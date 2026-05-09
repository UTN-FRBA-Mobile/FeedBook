package com.example.feedbook.features.notifications.data.repository

import com.example.feedbook.features.notifications.data.mapper.toDomain
import com.example.feedbook.features.notifications.data.remote.NotificationsRemoteDataSource
import com.example.feedbook.features.notifications.domain.model.NotificationsFeed
import com.example.feedbook.features.notifications.domain.repository.NotificationsRepository

class NotificationsRepositoryImpl(
    private val remoteDataSource: NotificationsRemoteDataSource
) : NotificationsRepository {
    override suspend fun getNotifications(): NotificationsFeed =
        remoteDataSource.getNotifications().toDomain()
}
