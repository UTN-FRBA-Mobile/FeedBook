package com.example.feedbook.features.stats.data.remote

import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class StatsRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
) {
    suspend fun getStats(): StatsDto = fakeBackend.getStats()
}
