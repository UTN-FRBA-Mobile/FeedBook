package com.example.feedbook.features.notifications.domain.repository

import com.example.feedbook.features.notifications.domain.model.NotificationsFeed

interface NotificationsRepository {
    suspend fun getNotifications(): NotificationsFeed
}
