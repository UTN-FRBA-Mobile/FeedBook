package com.example.feedbook.features.notifications.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.notifications.domain.model.FollowedYouNotification
import com.example.feedbook.features.notifications.domain.model.LikedYourReviewNotification
import com.example.feedbook.features.notifications.domain.model.NotificationActor
import com.example.feedbook.features.notifications.domain.model.NotificationBookSummary
import com.example.feedbook.features.notifications.domain.model.NotificationEntry
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.model.ReviewedBookNotification
import com.example.feedbook.features.notifications.domain.model.SavedYourBookNotification
import com.example.feedbook.features.notifications.domain.model.StartedReadingNotification
import com.example.feedbook.features.notifications.domain.model.UnknownNotification
import com.example.feedbook.features.profile.presentation.AvatarStyle

fun NotificationsFeed.toUiState(
    avatarStyle: AvatarStyle,
    avatarImageUri: String?
): NotificationsUiState = NotificationsUiState(
    title = title,
    items = items.map(NotificationEntry::toUiItem),
    avatarStyle = avatarStyle,
    avatarImageUri = avatarImageUri
)

private fun NotificationEntry.toUiItem(): NotificationItem = when (this) {
    is FollowedYouNotification -> NotificationItem.FollowedYou(
        actor = actor.toUi(),
        timestamp = timestamp
    )
    is StartedReadingNotification -> NotificationItem.StartedReading(
        actor = actor.toUi(),
        timestamp = timestamp,
        book = book.toUi()
    )
    is ReviewedBookNotification -> NotificationItem.ReviewedBook(
        actor = actor.toUi(),
        timestamp = timestamp,
        book = book.toUi()
    )
    is LikedYourReviewNotification -> NotificationItem.LikedYourReview(
        actor = actor.toUi(),
        timestamp = timestamp
    )
    is SavedYourBookNotification -> NotificationItem.SavedYourBook(
        actor = actor.toUi(),
        timestamp = timestamp,
        book = book.toUi()
    )
    is UnknownNotification -> NotificationItem.Generic(
        actor = actor.toUi(),
        timestamp = timestamp,
        fallbackText = fallbackText
    )
}

private fun NotificationActor.toUi(): NotificationActorUi = NotificationActorUi(
    name = name,
    avatarTopColor = Color(avatarTopColorHex),
    avatarBottomColor = Color(avatarBottomColorHex)
)

private fun NotificationBookSummary.toUi(): NotificationBookUi = NotificationBookUi(
    title = title,
    author = author,
    coverImageUrl = coverImageUrl
)
