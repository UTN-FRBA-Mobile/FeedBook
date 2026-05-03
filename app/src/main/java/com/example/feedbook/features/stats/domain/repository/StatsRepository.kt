package com.example.feedbook.features.stats.domain.repository

import com.example.feedbook.features.stats.domain.model.ReadingStats

interface StatsRepository {
    suspend fun getStats(): ReadingStats
}
