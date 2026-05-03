package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.profile.presentation.AvatarStyle

fun NotificationsFeed.toUiState(
    avatarStyle: AvatarStyle,
    avatarImageUri: String?
): NotificationsUiState = NotificationsUiState(
    title = title,
    items = items.map { entry ->
        NotificationItem.FriendActivity(
            message = entry.message,
            timestamp = entry.timestamp,
            avatarTopColor = Color(entry.avatarTopColorHex),
            avatarBottomColor = Color(entry.avatarBottomColorHex),
            badge = entry.badge,
            bookPreview = entry.bookPreview?.let {
                BookPreview(
                    title = it.title,
                    author = it.author,
                    accent = Color(it.accentHex)
                )
            }
        )
    },
    avatarStyle = avatarStyle,
    avatarImageUri = avatarImageUri
)
