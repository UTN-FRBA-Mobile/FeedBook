package com.example.feedbook.features.stats.data.remote

import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import com.example.feedbook.core.network.ApiService

class StatsRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getStats(): StatsDto = apiService.getStats()
}
