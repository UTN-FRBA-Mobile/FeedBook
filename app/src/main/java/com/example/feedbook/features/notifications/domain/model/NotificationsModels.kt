package com.example.feedbook.features.notifications.domain.model

data class NotificationsFeed(
    val title: String,
    val items: List<NotificationEntry>
)

data class NotificationEntry(
    val message: String,
    val timestamp: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val badge: String?,
    val bookPreview: BookPreview?
)

data class BookPreview(
    val title: String,
    val author: String,
    val accentHex: Long
)
