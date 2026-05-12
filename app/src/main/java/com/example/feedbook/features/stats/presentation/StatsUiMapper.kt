package com.example.feedbook.features.stats.presentation

import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.stats.domain.model.ReadingStats

fun ReadingStats.toUiState(
    avatarStyle: AvatarStyle,
    avatarPreset: AvatarPreset?,
    avatarImageUri: String?
): StatsUiState {
    val (normalizedRows, normalizedValues) = normalizeHeatmapRows(
        rows = heatmapRows,
        values = heatmapValues
    )

    return StatsUiState(
        title = title,
        subtitle = subtitle,
        metrics = metrics.map { StatsMetric(it.label, it.value) },
        heatmapMonths = heatmapMonths,
        heatmapRows = normalizedRows,
        heatmapValues = normalizedValues,
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
        avatarPreset = avatarPreset,
        avatarImageUri = avatarImageUri
    )
}

private fun normalizeHeatmapRows(
    rows: List<String>,
    values: List<List<Float>>
): Pair<List<String>, List<List<Float>>> {
    if (rows.size >= 7 && values.size >= 7) {
        return rows to values
    }

    val expectedColumns = values.maxOfOrNull { it.size } ?: 0
    val normalizedRows = rows.toMutableList()
    val normalizedValues = values.toMutableList()

    while (normalizedRows.size < 7) {
        val nextLabel = when (normalizedRows.size) {
            6 -> "D"
            else -> "?"
        }
        normalizedRows += nextLabel
    }

    while (normalizedValues.size < normalizedRows.size) {
        normalizedValues += List(expectedColumns) { 0f }
    }

    return normalizedRows to normalizedValues
}
