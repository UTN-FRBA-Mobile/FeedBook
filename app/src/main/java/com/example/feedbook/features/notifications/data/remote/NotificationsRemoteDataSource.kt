package com.example.feedbook.features.notifications.data.remote

import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class NotificationsRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
) {
    suspend fun getNotifications(): NotificationsDto = fakeBackend.getNotifications()
}
