package com.example.feedbook.features.stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import com.example.feedbook.features.stats.presentation.HeatmapMeaning
import com.example.feedbook.features.stats.presentation.HeatmapScale
import com.example.feedbook.features.stats.presentation.HeatmapScaleLevel
import kotlin.math.ceil
import kotlin.math.min

@Composable
internal fun ReadingHeatmapCard(
    months: List<String>,
    rows: List<String>,
    values: List<List<Float>>,
    scale: HeatmapScale,
    modifier: Modifier = Modifier
) {
    var showLegend by remember { mutableStateOf(false) }
    var selectedCell by remember(months, rows, values) { mutableStateOf<HeatmapCellDescriptor?>(null) }

    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.stats_daily_cadence),
                    style = ProfileTypography.SectionTitle.copy(fontSize = 26.sp),
                    color = ProfileColors.PrimaryText
                )

                Box {
                    IconButton(
                        onClick = { showLegend = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.stats_heatmap_info),
                            tint = ProfileColors.SecondaryText
                        )
                    }

                    DropdownMenu(
                        expanded = showLegend,
                        onDismissRequest = { showLegend = false },
                        modifier = Modifier.background(ProfileColors.Surface)
                    ) {
                        scale.levels.forEach { level ->
                            HeatmapLegendEntry(
                                color = level.color,
                                label = level.label
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ProfileColors.Background)
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        months.forEach { month ->
                            Text(
                                text = month,
                                style = ProfileTypography.Label,
                                color = ProfileColors.SecondaryText
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 2.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rows.forEach { label ->
                                Box(
                                    modifier = Modifier.height(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = ProfileTypography.Label.copy(fontSize = 9.sp, lineHeight = 9.sp),
                                        color = ProfileColors.SecondaryText
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            values.forEachIndexed { rowIndex, week ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    week.forEachIndexed { columnIndex, intensity ->
                                        val cell = rememberHeatmapCellDescriptor(
                                            rowIndex = rowIndex,
                                            columnIndex = columnIndex,
                                            rows = rows,
                                            months = months,
                                            totalColumns = week.size,
                                            intensity = intensity,
                                            scale = scale
                                        )
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(12.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(cell.level.color)
                                                .border(
                                                    width = if (selectedCell == cell) 1.dp else 0.dp,
                                                    color = if (selectedCell == cell) ProfileColors.SurfaceStrong else Color.Transparent,
                                                    shape = RoundedCornerShape(2.dp)
                                                )
                                                .semantics {
                                                    contentDescription = cell.accessibilityLabel
                                                }
                                                .clickable { selectedCell = cell }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            selectedCell?.let { cell ->
                SelectedHeatmapDaySummary(cell = cell)
            } ?: run {
                Text(
                    text = "Tap a square to inspect the day cadence.",
                    style = ProfileTypography.Label.copy(fontSize = 11.sp, lineHeight = 14.sp),
                    color = ProfileColors.SecondaryText
                )
            }
        }
    }
}

@Composable
private fun HeatmapLegendEntry(
    color: Color,
    label: String
) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = label,
                    style = ProfileTypography.Label.copy(fontSize = 11.sp, lineHeight = 14.sp),
                    color = ProfileColors.PrimaryText
                )
            }
        },
        onClick = {}
    )
}

@Composable
private fun SelectedHeatmapDaySummary(cell: HeatmapCellDescriptor) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(cell.level.color)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "${cell.monthLabel} · ${cell.dayLabel} · Week ${cell.weekOfMonth}",
                style = ProfileTypography.LabelUppercase.copy(fontSize = 9.sp, lineHeight = 11.sp),
                color = ProfileColors.SecondaryText
            )
            Text(
                text = cell.level.label,
                style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 18.sp),
                color = ProfileColors.PrimaryText
            )
        }
    }
}

private data class HeatmapCellDescriptor(
    val dayLabel: String,
    val monthLabel: String,
    val weekOfMonth: Int,
    val intensity: Float,
    val level: HeatmapScaleLevel
) {
    val accessibilityLabel: String
        get() = "$dayLabel, $monthLabel, week $weekOfMonth, ${level.label}"
}

private fun rememberHeatmapCellDescriptor(
    rowIndex: Int,
    columnIndex: Int,
    rows: List<String>,
    months: List<String>,
    totalColumns: Int,
    intensity: Float,
    scale: HeatmapScale
): HeatmapCellDescriptor {
    val dayLabel = rows.getOrElse(rowIndex) { "?" }
    val monthSlotSize = ceil(totalColumns / months.size.toFloat()).toInt().coerceAtLeast(1)
    val monthIndex = min(columnIndex / monthSlotSize, months.lastIndex)
    val monthLabel = months.getOrElse(monthIndex) { "" }
    val weekOfMonth = (columnIndex - (monthIndex * monthSlotSize)) + 1

    return HeatmapCellDescriptor(
        dayLabel = dayLabel,
        monthLabel = monthLabel,
        weekOfMonth = weekOfMonth,
        intensity = intensity,
        level = scale.levelFor(intensity)
    )
}
