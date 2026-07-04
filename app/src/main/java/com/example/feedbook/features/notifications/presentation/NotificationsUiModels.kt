package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class NotificationsUiState(
    val title: String,
    val items: List<NotificationItem>,
    val avatarStyle: AvatarStyle,
    val avatarPreset: AvatarPreset?,
    val avatarImageUri: String?,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface NotificationItem {
    val actor: NotificationActorUi
    val timestamp: String

    data class FollowedYou(
        override val actor: NotificationActorUi,
        override val timestamp: String
    ) : NotificationItem

    data class StartedReading(
        override val actor: NotificationActorUi,
        override val timestamp: String,
        val book: NotificationBookUi
    ) : NotificationItem

    data class ReviewedBook(
        override val actor: NotificationActorUi,
        override val timestamp: String,
        val book: NotificationBookUi
    ) : NotificationItem

    data class LikedYourReview(
        override val actor: NotificationActorUi,
        override val timestamp: String
    ) : NotificationItem

    data class SavedYourBook(
        override val actor: NotificationActorUi,
        override val timestamp: String,
        val book: NotificationBookUi
    ) : NotificationItem

    data class Generic(
        override val actor: NotificationActorUi,
        override val timestamp: String,
        val fallbackText: String
    ) : NotificationItem
}

data class NotificationActorUi(
    val name: String,
    val avatarImageUri: String?,
    val avatarTopColor: Color,
    val avatarBottomColor: Color
)

data class NotificationBookUi(
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

fun emptyNotificationsUiState(): NotificationsUiState = NotificationsUiState(
    title = "",
    items = emptyList(),
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null
)
