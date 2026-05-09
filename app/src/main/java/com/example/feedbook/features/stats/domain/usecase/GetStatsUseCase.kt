package com.example.feedbook.features.stats.domain.usecase

import com.example.feedbook.features.stats.domain.model.ReadingStats
import com.example.feedbook.features.stats.domain.repository.StatsRepository

class GetStatsUseCase(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(): ReadingStats = repository.getStats()
}
