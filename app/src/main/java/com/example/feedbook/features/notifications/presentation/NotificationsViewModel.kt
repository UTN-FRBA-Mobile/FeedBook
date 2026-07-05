package com.example.feedbook.features.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val refreshBus: UserContentRefreshBus,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        emptyNotificationsUiState().copy(isLoading = true)
    )
    val state: StateFlow<NotificationsUiState> = _state.asStateFlow()

    private var baseFeed: NotificationsFeed? = null
    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        preset = null,
        imageUri = null
    )

    init {
        viewModelScope.launch {
            refreshBus.version.collectLatest {
                loadNotifications()
            }
        }

        viewModelScope.launch {
            observeOwnProfileUseCase()
                .catch { }
                .collectLatest { profile ->
                    avatarPresentation = profile.toAvatarPresentation()
                    emitNotifications()
                }
        }
    }

    fun retry() {
        refreshBus.refresh()
    }

    private suspend fun loadNotifications() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        try {
            baseFeed = getNotificationsUseCase()
            emitNotifications()
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }
            _state.value = emptyNotificationsUiState().copy(
                avatarStyle = avatarPresentation.style,
                avatarPreset = avatarPresentation.preset,
                avatarImageUri = avatarPresentation.imageUri,
                isLoading = false,
                errorMessage = throwable.message
            )
        }
    }

    private fun emitNotifications() {
        val feed = baseFeed ?: return
        _state.value = feed.toUiState(
            avatarStyle = avatarPresentation.style,
            avatarPreset = avatarPresentation.preset,
            avatarImageUri = avatarPresentation.imageUri
        )
            .copy(isLoading = false, errorMessage = null)
    }

    companion object {
        fun provideFactory(
            getNotificationsUseCase: GetNotificationsUseCase,
            refreshBus: UserContentRefreshBus,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationsViewModel(
                    getNotificationsUseCase = getNotificationsUseCase,
                    refreshBus = refreshBus,
                    observeOwnProfileUseCase = observeOwnProfileUseCase
                ) as T
            }
        }
    }
}
