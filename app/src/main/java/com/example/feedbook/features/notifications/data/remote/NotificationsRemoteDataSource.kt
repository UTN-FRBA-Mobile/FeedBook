package com.example.feedbook.features.notifications.data.remote

import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.core.network.ApiService

class NotificationsRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getNotifications(): NotificationsDto = apiService.getNotifications()
}
