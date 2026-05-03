package com.example.feedbook.features.notifications.data.mapper

import com.example.feedbook.features.notifications.data.remote.dto.NotificationBookPreviewDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationEntryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.notifications.domain.model.BookPreview
import com.example.feedbook.features.notifications.domain.model.NotificationEntry
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed

fun NotificationsDto.toDomain(): NotificationsFeed = NotificationsFeed(
    title = title,
    items = items.map(NotificationEntryDto::toDomain)
)

private fun NotificationEntryDto.toDomain(): NotificationEntry = NotificationEntry(
    message = message,
    timestamp = timestamp,
    avatarTopColorHex = avatarTopColorHex,
    avatarBottomColorHex = avatarBottomColorHex,
    badge = badge,
    bookPreview = bookPreview?.toDomain()
)

private fun NotificationBookPreviewDto.toDomain(): BookPreview = BookPreview(title, author, accentHex)
