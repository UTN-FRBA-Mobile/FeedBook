package com.example.feedbook.features.notifications.data.remote.dto

data class NotificationsDto(
    val title: String,
    val items: List<NotificationEntryDto>
)

data class NotificationEntryDto(
    val message: String,
    val timestamp: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val badge: String?,
    val bookPreview: NotificationBookPreviewDto?
)

data class NotificationBookPreviewDto(
    val title: String,
    val author: String,
    val accentHex: Long
)
