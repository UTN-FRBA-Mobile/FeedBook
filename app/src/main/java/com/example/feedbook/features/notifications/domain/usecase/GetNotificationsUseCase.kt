package com.example.feedbook.features.notifications.domain.usecase

import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.repository.NotificationsRepository

class GetNotificationsUseCase(
    private val repository: NotificationsRepository
) {
    suspend operator fun invoke(): NotificationsFeed = repository.getNotifications()
}
