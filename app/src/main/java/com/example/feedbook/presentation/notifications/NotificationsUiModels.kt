package com.example.feedbook.presentation.notifications

import androidx.compose.ui.graphics.Color

data class NotificationsUiState(
    val title: String,
    val items: List<NotificationItem>
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
            message = "A Juan le gustó tu reseña.\n\"Una exploración fascinante sobre alimentación y memoria. La prosa fluye\"",
            timestamp = "HOY · 10:24",
            avatarTopColor = Color(0xFF35566F),
            avatarBottomColor = Color(0xFFC8A988),
            badge = "♥"
        ),
        NotificationItem.FriendActivity(
            message = "Sofía comenzó a seguirte.",
            timestamp = "HOY · 07:12",
            avatarTopColor = Color(0xFFB9CBE3),
            avatarBottomColor = Color(0xFFE7EEF7),
            badge = null
        ),
        NotificationItem.FriendActivity(
            message = "Elena compartió un nuevo libro.",
            timestamp = "AYER · 14:30",
            avatarTopColor = Color(0xFF534D61),
            avatarBottomColor = Color(0xFFD9B89C),
            badge = "⇪",
            bookPreview = BookPreview(
                title = "El Laberinto de los Espíritus",
                author = "CARLOS RUIZ ZAFÓN",
                accent = Color(0xFFD6E1EB)
            )
        ),
        NotificationItem.FriendActivity(
            message = "Martina comentó tu estado de lectura.\n\"Ese final me dejó pensando días.\"",
            timestamp = "AYER · 09:18",
            avatarTopColor = Color(0xFF6D7FA2),
            avatarBottomColor = Color(0xFFDAB596),
            badge = "✦"
        ),
        NotificationItem.FriendActivity(
            message = "Tomás empezó a seguirte.",
            timestamp = "LUNES · 21:04",
            avatarTopColor = Color(0xFF4E697F),
            avatarBottomColor = Color(0xFFE6C7AA)
        ),
        NotificationItem.FriendActivity(
            message = "Lucía guardó uno de tus libros en su lista de lectura.",
            timestamp = "LUNES · 17:42",
            avatarTopColor = Color(0xFF7A8B6A),
            avatarBottomColor = Color(0xFFDCC6A7),
            badge = "⌁"
        ),
        NotificationItem.FriendActivity(
            message = "A Bruno le gustó tu reseña de \"Beloved\".",
            timestamp = "DOMINGO · 19:26",
            avatarTopColor = Color(0xFF5A556A),
            avatarBottomColor = Color(0xFFCDA58B),
            badge = "♥"
        ),
        NotificationItem.FriendActivity(
            message = "Camila compartió un nuevo libro.",
            timestamp = "DOMINGO · 11:03",
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
            message = "Nicolás comenzó a seguirte.",
            timestamp = "SÁBADO · 16:58",
            avatarTopColor = Color(0xFF4D6B73),
            avatarBottomColor = Color(0xFFD3B08C)
        ),
        NotificationItem.FriendActivity(
            message = "A Irene le gustó tu cita destacada de \"The Waves\".",
            timestamp = "SÁBADO · 08:41",
            avatarTopColor = Color(0xFF607D8B),
            avatarBottomColor = Color(0xFFE5CDB4),
            badge = "♥"
        )
    )
)
