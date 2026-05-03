package com.example.feedbook.features.stats.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.stats.domain.model.ReadingStats

fun ReadingStats.toUiState(
    avatarStyle: AvatarStyle,
    avatarImageUri: String?
): StatsUiState = StatsUiState(
    title = title,
    subtitle = subtitle,
    metrics = metrics.map { StatsMetric(it.label, it.value) },
    heatmapMonths = heatmapMonths,
    heatmapRows = heatmapRows,
    heatmapValues = heatmapValues,
    heatmapScale = defaultHeatmapScale(),
    selectedRadarMode = radarSections.firstOrNull()?.mode.orEmpty(),
    radarSections = radarSections.map { section ->
        RadarSection(
            mode = section.mode,
            axes = section.axes.map { RadarAxis(it.label, it.value) },
            ranking = section.ranking.map { RankingItem(it.rank, it.label) }
        )
    },
    avatarStyle = avatarStyle,
    avatarImageUri = avatarImageUri
)
