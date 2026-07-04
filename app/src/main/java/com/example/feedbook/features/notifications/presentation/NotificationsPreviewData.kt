package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color

private fun previewCoverUrl(isbn: String): String =
    "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg"

fun previewNotificationsUiState(): NotificationsUiState = emptyNotificationsUiState().copy(
    title = "Activity and Notifications",
    items = listOf(
        NotificationItem.LikedYourReview(
            actor = NotificationActorUi(
                name = "Juan",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF35566F),
                avatarBottomColor = Color(0xFFC8A988)
            ),
            timestamp = "TODAY · 10:24",
        ),
        NotificationItem.FollowedYou(
            actor = NotificationActorUi(
                name = "Sofía",
                avatarImageUri = null,
                avatarTopColor = Color(0xFFB9CBE3),
                avatarBottomColor = Color(0xFFE7EEF7)
            ),
            timestamp = "TODAY · 07:12",
        ),
        NotificationItem.ReviewedBook(
            actor = NotificationActorUi(
                name = "Elena",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF534D61),
                avatarBottomColor = Color(0xFFD9B89C)
            ),
            timestamp = "YESTERDAY · 14:30",
            book = NotificationBookUi(
                title = "The Labyrinth of the Spirits",
                author = "CARLOS RUIZ ZAFÓN",
                coverImageUrl = previewCoverUrl("9788408163381")
            )
        ),
        NotificationItem.StartedReading(
            actor = NotificationActorUi(
                name = "Martina",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF6D7FA2),
                avatarBottomColor = Color(0xFFDAB596)
            ),
            timestamp = "YESTERDAY · 09:18",
            book = NotificationBookUi(
                title = "The Left Hand of Darkness",
                author = "URSULA K. LE GUIN",
                coverImageUrl = previewCoverUrl("9780441478125")
            )
        ),
        NotificationItem.FollowedYou(
            actor = NotificationActorUi(
                name = "Tomás",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF4E697F),
                avatarBottomColor = Color(0xFFE6C7AA)
            ),
            timestamp = "MONDAY · 21:04",
        ),
        NotificationItem.SavedYourBook(
            actor = NotificationActorUi(
                name = "Lucía",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF7A8B6A),
                avatarBottomColor = Color(0xFFDCC6A7)
            ),
            timestamp = "MONDAY · 17:42",
            book = NotificationBookUi(
                title = "Beloved",
                author = "TONI MORRISON",
                coverImageUrl = previewCoverUrl("9781400033416")
            )
        ),
        NotificationItem.LikedYourReview(
            actor = NotificationActorUi(
                name = "Bruno",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF5A556A),
                avatarBottomColor = Color(0xFFCDA58B)
            ),
            timestamp = "SUNDAY · 19:26",
        ),
        NotificationItem.ReviewedBook(
            actor = NotificationActorUi(
                name = "Camila",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF7D6B8D),
                avatarBottomColor = Color(0xFFE2C39F)
            ),
            timestamp = "SUNDAY · 11:03",
            book = NotificationBookUi(
                title = "Piranesi",
                author = "SUSANNA CLARKE",
                coverImageUrl = previewCoverUrl("9781635575637")
            )
        ),
        NotificationItem.FollowedYou(
            actor = NotificationActorUi(
                name = "Nicolás",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF4D6B73),
                avatarBottomColor = Color(0xFFD3B08C)
            ),
            timestamp = "SATURDAY · 16:58",
        ),
        NotificationItem.Generic(
            actor = NotificationActorUi(
                name = "Irene",
                avatarImageUri = null,
                avatarTopColor = Color(0xFF607D8B),
                avatarBottomColor = Color(0xFFE5CDB4)
            ),
            timestamp = "SATURDAY · 08:41",
            fallbackText = "Irene liked your featured quote from \"The Waves\"."
        )
    )
)
