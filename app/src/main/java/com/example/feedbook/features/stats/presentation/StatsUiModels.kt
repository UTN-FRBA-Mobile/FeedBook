package com.example.feedbook.features.stats.presentation

import androidx.compose.ui.graphics.Color
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
    fun colorFor(value: Float): Color {
        val normalized = value.coerceIn(0f, 1f)
        return levels.lastOrNull { normalized >= it.minValue }?.color
            ?: levels.firstOrNull()?.color
            ?: Color.Unspecified
    }
}

data class HeatmapScaleLevel(
    val minValue: Float,
    val color: Color,
    val label: String
)

fun defaultHeatmapScale(): HeatmapScale = HeatmapScale(
    levels = listOf(
        HeatmapScaleLevel(
            minValue = 0f,
            color = Color(0xFFF3EFEB),
            label = "No leiste"
        ),
        HeatmapScaleLevel(
            minValue = 0.12f,
            color = Color(0xFFE0D0BC),
            label = "Mucho menos que el objetivo"
        ),
        HeatmapScaleLevel(
            minValue = 0.35f,
            color = Color(0xFFC5A583),
            label = "Casi llegas al objetivo"
        ),
        HeatmapScaleLevel(
            minValue = 0.60f,
            color = Color(0xFF7B8EA3),
            label = "Objetivo cumplido"
        ),
        HeatmapScaleLevel(
            minValue = 0.82f,
            color = Color(0xFF32475E),
            label = "Superas el objetivo"
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
