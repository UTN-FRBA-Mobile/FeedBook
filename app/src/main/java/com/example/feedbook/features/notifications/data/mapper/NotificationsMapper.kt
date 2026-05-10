package com.example.feedbook.features.notifications.data.mapper

import com.example.feedbook.features.notifications.data.remote.dto.NotificationActorDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationEntryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationBookSummaryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationTypes
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
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

fun NotificationsDto.toDomain(): NotificationsFeed = NotificationsFeed(
    title = title,
    items = items.map(NotificationEntryDto::toDomain)
)

private fun NotificationEntryDto.toDomain(): NotificationEntry {
    val actorDomain = actor.toDomain()
    return when (type) {
        NotificationTypes.FOLLOWED_YOU -> FollowedYouNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp
        )
        NotificationTypes.STARTED_READING -> StartedReadingNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp,
            book = requireNotNull(book).toDomain()
        )
        NotificationTypes.REVIEWED_BOOK -> ReviewedBookNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp,
            book = requireNotNull(book).toDomain()
        )
        NotificationTypes.LIKED_YOUR_REVIEW -> LikedYourReviewNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp
        )
        NotificationTypes.SAVED_YOUR_BOOK -> SavedYourBookNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp,
            book = requireNotNull(book).toDomain()
        )
        else -> UnknownNotification(
            id = id,
            actor = actorDomain,
            timestamp = timestamp,
            fallbackText = fallbackText
        )
    }
}

private fun NotificationActorDto.toDomain(): NotificationActor = NotificationActor(
    name = name,
    avatarTopColorHex = avatarTopColorHex,
    avatarBottomColorHex = avatarBottomColorHex
)

private fun NotificationBookSummaryDto.toDomain(): NotificationBookSummary =
    NotificationBookSummary(title, author, coverImageUrl)
