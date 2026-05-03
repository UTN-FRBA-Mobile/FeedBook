package com.example.feedbook.features.stats.domain.model

data class ReadingStats(
    val title: String,
    val subtitle: String,
    val metrics: List<StatsMetric>,
    val heatmapMonths: List<String>,
    val heatmapRows: List<String>,
    val heatmapValues: List<List<Float>>,
    val radarSections: List<RadarSection>
)

data class StatsMetric(
    val label: String,
    val value: String
)

data class RadarSection(
    val mode: String,
    val axes: List<RadarAxis>,
    val ranking: List<RankingItem>
)

data class RadarAxis(
    val label: String,
    val value: Float
)

data class RankingItem(
    val rank: Int,
    val label: String
)
