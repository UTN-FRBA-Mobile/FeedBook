package com.example.feedbook.features.stats.data.repository

import com.example.feedbook.features.stats.data.mapper.toDomain
import com.example.feedbook.features.stats.data.remote.StatsRemoteDataSource
import com.example.feedbook.features.stats.domain.model.ReadingStats
import com.example.feedbook.features.stats.domain.repository.StatsRepository

class StatsRepositoryImpl(
    private val remoteDataSource: StatsRemoteDataSource
) : StatsRepository {
    override suspend fun getStats(): ReadingStats = remoteDataSource.getStats().toDomain()
}
