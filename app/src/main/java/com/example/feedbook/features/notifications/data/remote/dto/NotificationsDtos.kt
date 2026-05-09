package com.example.feedbook.features.notifications.data.remote.dto

data class NotificationsDto(
    val title: String,
    val items: List<NotificationEntryDto>
)

data class NotificationEntryDto(
    val id: String,
    val type: String,
    val timestamp: String,
    val actor: NotificationActorDto,
    val book: NotificationBookSummaryDto? = null,
    val fallbackText: String
)

data class NotificationActorDto(
    val name: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long
)

data class NotificationBookSummaryDto(
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

object NotificationTypes {
    const val FOLLOWED_YOU = "followed_you"
    const val STARTED_READING = "started_reading"
    const val REVIEWED_BOOK = "reviewed_book"
    const val LIKED_YOUR_REVIEW = "liked_your_review"
    const val SAVED_YOUR_BOOK = "saved_your_book"
}
