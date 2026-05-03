package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(sampleNotificationsUiState())
    val state: StateFlow<NotificationsUiState> = _state.asStateFlow()

    private var baseFeed: NotificationsFeed? = null
    private var currentAvatarStyle: AvatarStyle = sampleNotificationsUiState().avatarStyle
    private var currentAvatarImageUri: String? = null

    init {
        viewModelScope.launch {
            runCatching { getNotificationsUseCase() }
                .onSuccess {
                    baseFeed = it
                    emitNotifications()
                }
        }

        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                currentAvatarStyle = profile.toAvatarStyle()
                currentAvatarImageUri = profile.avatar.imageUri
                emitNotifications()
            }
        }
    }

    private fun emitNotifications() {
        val feed = baseFeed ?: return
        _state.value = feed.toUiState(
            avatarStyle = currentAvatarStyle,
            avatarImageUri = currentAvatarImageUri
        )
    }

    private fun ReaderProfile.toAvatarStyle(): AvatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )

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
