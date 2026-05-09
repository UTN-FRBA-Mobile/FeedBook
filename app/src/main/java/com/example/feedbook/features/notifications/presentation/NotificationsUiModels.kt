package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarStyle

data class NotificationsUiState(
    val title: String,
    val items: List<NotificationItem>,
    val avatarStyle: AvatarStyle,
    val avatarImageUri: String?
)

sealed interface NotificationItem {
    data class FriendActivity(
        val message: String,
        val timestamp: String,
        val avatarTopColor: Color,
        val avatarBottomColor: Color,
        val badge: String? = null,
        val bookPreview: BookPreview? = null
    ) : NotificationItem
}

data class BookPreview(
    val title: String,
    val author: String,
    val accent: Color
)

fun sampleNotificationsUiState(): NotificationsUiState = NotificationsUiState(
    title = "Activity and Notifications",
    items = listOf(
        NotificationItem.FriendActivity(
            message = "Juan liked your review.\n\"A fascinating exploration of nourishment and memory. The prose moves effortlessly.\"",
            timestamp = "TODAY · 10:24",
            avatarTopColor = Color(0xFF35566F),
            avatarBottomColor = Color(0xFFC8A988),
            badge = "♥"
        ),
        NotificationItem.FriendActivity(
            message = "Sofia started following you.",
            timestamp = "TODAY · 07:12",
            avatarTopColor = Color(0xFFB9CBE3),
            avatarBottomColor = Color(0xFFE7EEF7),
            badge = null
        ),
        NotificationItem.FriendActivity(
            message = "Elena shared a new book.",
            timestamp = "YESTERDAY · 14:30",
            avatarTopColor = Color(0xFF534D61),
            avatarBottomColor = Color(0xFFD9B89C),
            badge = "⇪",
            bookPreview = BookPreview(
                title = "The Labyrinth of Spirits",
                author = "CARLOS RUIZ ZAFON",
                accent = Color(0xFFD6E1EB)
            )
        ),
        NotificationItem.FriendActivity(
            message = "Martina commented on your reading status.\n\"That ending stayed with me for days.\"",
            timestamp = "YESTERDAY · 09:18",
            avatarTopColor = Color(0xFF6D7FA2),
            avatarBottomColor = Color(0xFFDAB596),
            badge = "✦"
        ),
        NotificationItem.FriendActivity(
            message = "Tomas started following you.",
            timestamp = "MONDAY · 21:04",
            avatarTopColor = Color(0xFF4E697F),
            avatarBottomColor = Color(0xFFE6C7AA)
        ),
        NotificationItem.FriendActivity(
            message = "Lucia saved one of your books to her reading list.",
            timestamp = "MONDAY · 17:42",
            avatarTopColor = Color(0xFF7A8B6A),
            avatarBottomColor = Color(0xFFDCC6A7),
            badge = "⌁"
        ),
        NotificationItem.FriendActivity(
            message = "Bruno liked your review of \"Beloved\".",
            timestamp = "SUNDAY · 19:26",
            avatarTopColor = Color(0xFF5A556A),
            avatarBottomColor = Color(0xFFCDA58B),
            badge = "♥"
        ),
        NotificationItem.FriendActivity(
            message = "Camila shared a new book.",
            timestamp = "SUNDAY · 11:03",
            avatarTopColor = Color(0xFF7D6B8D),
            avatarBottomColor = Color(0xFFE2C39F),
            badge = "⇪",
            bookPreview = BookPreview(
                title = "Piranesi",
                author = "SUSANNA CLARKE",
                accent = Color(0xFFC7D6DD)
            )
        ),
        NotificationItem.FriendActivity(
            message = "Nicolas started following you.",
            timestamp = "SATURDAY · 16:58",
            avatarTopColor = Color(0xFF4D6B73),
            avatarBottomColor = Color(0xFFD3B08C)
        ),
        NotificationItem.FriendActivity(
            message = "Irene liked your highlighted quote from \"The Waves\".",
            timestamp = "SATURDAY · 08:41",
            avatarTopColor = Color(0xFF607D8B),
            avatarBottomColor = Color(0xFFE5CDB4),
            badge = "♥"
        )
    ),
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarImageUri = null
)
