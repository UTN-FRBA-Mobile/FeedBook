package com.example.feedbook.features.notifications.domain.model

data class NotificationsFeed(
    val title: String,
    val items: List<NotificationEntry>
)

data class NotificationActor(
    val name: String,
    val avatarImageUrl: String?,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long
)

data class NotificationBookSummary(
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

sealed interface NotificationEntry {
    val id: String
    val actor: NotificationActor
    val timestamp: String
}

data class FollowedYouNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String
) : NotificationEntry

data class StartedReadingNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String,
    val book: NotificationBookSummary
) : NotificationEntry

data class ReviewedBookNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String,
    val book: NotificationBookSummary
) : NotificationEntry

data class LikedYourReviewNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String
) : NotificationEntry

data class SavedYourBookNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String,
    val book: NotificationBookSummary
) : NotificationEntry

data class UnknownNotification(
    override val id: String,
    override val actor: NotificationActor,
    override val timestamp: String,
    val fallbackText: String
) : NotificationEntry
