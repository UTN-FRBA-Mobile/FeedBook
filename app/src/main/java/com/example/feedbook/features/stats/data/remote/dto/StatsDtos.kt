package com.example.feedbook.features.stats.data.remote.dto

data class StatsDto(
    val title: String,
    val subtitle: String,
    val metrics: List<StatsMetricDto>,
    val heatmapMonths: List<String>,
    val heatmapRows: List<String>,
    val heatmapValues: List<List<Float>>,
    val radarSections: List<RadarSectionDto>
)

data class StatsMetricDto(
    val label: String,
    val value: String
)

data class RadarSectionDto(
    val mode: String,
    val axes: List<RadarAxisDto>,
    val ranking: List<RankingItemDto>
)

data class RadarAxisDto(
    val label: String,
    val value: Float
)

data class RankingItemDto(
    val rank: Int,
    val label: String
)
