package com.example.feedbook.features.stats.data.mapper

import com.example.feedbook.features.stats.data.remote.dto.*
import com.example.feedbook.features.stats.domain.model.*

fun StatsDto.toDomain(): ReadingStats = ReadingStats(
    title = title,
    subtitle = subtitle,
    metrics = metrics.map(StatsMetricDto::toDomain),
    heatmapMonths = heatmapMonths,
    heatmapRows = heatmapRows,
    heatmapValues = heatmapValues,
    radarSections = radarSections.map(RadarSectionDto::toDomain)
)

private fun StatsMetricDto.toDomain(): StatsMetric = StatsMetric(label, value)
private fun RadarSectionDto.toDomain(): RadarSection = RadarSection(mode, axes.map(RadarAxisDto::toDomain), ranking.map(RankingItemDto::toDomain))
private fun RadarAxisDto.toDomain(): RadarAxis = RadarAxis(label, value)
private fun RankingItemDto.toDomain(): RankingItem = RankingItem(rank, label)
