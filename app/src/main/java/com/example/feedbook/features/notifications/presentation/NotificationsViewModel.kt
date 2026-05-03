package com.example.feedbook.features.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        emptyNotificationsUiState().copy(isLoading = true)
    )
    val state: StateFlow<NotificationsUiState> = _state.asStateFlow()

    private var baseFeed: NotificationsFeed? = null
    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        imageUri = null
    )

    init {
        loadNotifications()

        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                avatarPresentation = profile.toAvatarPresentation()
                emitNotifications()
            }
        }
    }

    fun retry() {
        loadNotifications()
    }

    private fun loadNotifications() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { getNotificationsUseCase() }
                .onSuccess {
                    baseFeed = it
                    emitNotifications()
                }
                .onFailure { throwable ->
                    _state.value = emptyNotificationsUiState().copy(
                        avatarStyle = avatarPresentation.style,
                        avatarImageUri = avatarPresentation.imageUri,
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
        }
    }

    private fun emitNotifications() {
        val feed = baseFeed ?: return
        _state.value = feed.toUiState(
            avatarStyle = avatarPresentation.style,
            avatarImageUri = avatarPresentation.imageUri
        )
            .copy(isLoading = false, errorMessage = null)
    }

    companion object {
        fun provideFactory(
            getNotificationsUseCase: GetNotificationsUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationsViewModel(
                    getNotificationsUseCase = getNotificationsUseCase,
                    observeOwnProfileUseCase = observeOwnProfileUseCase
                ) as T
            }
        }
    }
}
