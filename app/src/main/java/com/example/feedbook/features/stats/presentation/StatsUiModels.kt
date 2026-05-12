package com.example.feedbook.features.stats.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.core.ui.theme.FeedBookColors
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class StatsUiState(
    val title: String,
    val subtitle: String,
    val metrics: List<StatsMetric>,
    val heatmapMonths: List<String>,
    val heatmapRows: List<String>,
    val heatmapValues: List<List<Float>>,
    val heatmapScale: HeatmapScale,
    val selectedRadarMode: String,
    val radarSections: List<RadarSection>,
    val avatarStyle: AvatarStyle,
    val avatarPreset: AvatarPreset?,
    val avatarImageUri: String?,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class StatsMetric(
    val label: String,
    val value: String
)

data class RadarAxis(
    val label: String,
    val value: Float
)

data class RankingItem(
    val rank: Int,
    val label: String
)

data class RadarSection(
    val mode: String,
    val axes: List<RadarAxis>,
    val ranking: List<RankingItem>
)

data class HeatmapScale(
    val levels: List<HeatmapScaleLevel>
) {
    fun levelFor(value: Float): HeatmapScaleLevel {
        val normalized = value.coerceIn(0f, 1f)
        return levels.lastOrNull { normalized >= it.minValue }
            ?: levels.firstOrNull()
            ?: HeatmapScaleLevel(
                meaning = HeatmapMeaning.NO_READING,
                minValue = 0f,
                color = Color.Unspecified,
                label = HeatmapMeaning.NO_READING.defaultLabel
            )
    }

    fun colorFor(value: Float): Color {
        return levelFor(value).color
    }
}

enum class HeatmapMeaning(val defaultLabel: String) {
    NO_READING("No leiste"),
    BELOW_TARGET("Mucho menos que el objetivo"),
    NEAR_TARGET("Casi llegas al objetivo"),
    GOAL_MET("Objetivo cumplido"),
    ABOVE_TARGET("Superas el objetivo")
}

data class HeatmapScaleLevel(
    val meaning: HeatmapMeaning,
    val minValue: Float,
    val color: Color,
    val label: String
)

fun defaultHeatmapScale(): HeatmapScale = HeatmapScale(
    levels = listOf(
        HeatmapScaleLevel(
            meaning = HeatmapMeaning.NO_READING,
            minValue = 0f,
            color = FeedBookColors.HeatmapNoReading,
            label = HeatmapMeaning.NO_READING.defaultLabel
        ),
        HeatmapScaleLevel(
            meaning = HeatmapMeaning.BELOW_TARGET,
            minValue = 0.12f,
            color = FeedBookColors.HeatmapBelowTarget,
            label = HeatmapMeaning.BELOW_TARGET.defaultLabel
        ),
        HeatmapScaleLevel(
            meaning = HeatmapMeaning.NEAR_TARGET,
            minValue = 0.35f,
            color = FeedBookColors.HeatmapNearTarget,
            label = HeatmapMeaning.NEAR_TARGET.defaultLabel
        ),
        HeatmapScaleLevel(
            meaning = HeatmapMeaning.GOAL_MET,
            minValue = 0.60f,
            color = FeedBookColors.HeatmapGoalMet,
            label = HeatmapMeaning.GOAL_MET.defaultLabel
        ),
        HeatmapScaleLevel(
            meaning = HeatmapMeaning.ABOVE_TARGET,
            minValue = 0.82f,
            color = FeedBookColors.HeatmapAboveTarget,
            label = HeatmapMeaning.ABOVE_TARGET.defaultLabel
        )
    )
)

fun emptyStatsUiState(): StatsUiState = StatsUiState(
    title = "",
    subtitle = "",
    metrics = emptyList(),
    heatmapMonths = emptyList(),
    heatmapRows = emptyList(),
    heatmapValues = emptyList(),
    heatmapScale = defaultHeatmapScale(),
    selectedRadarMode = "",
    radarSections = emptyList(),
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null
)
