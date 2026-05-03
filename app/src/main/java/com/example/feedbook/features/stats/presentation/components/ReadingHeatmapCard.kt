package com.example.feedbook.features.stats.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
internal fun ReadingHeatmapCard(
    months: List<String>,
    rows: List<String>,
    values: List<List<Float>>,
    modifier: Modifier = Modifier
) {
    var showLegend by remember { mutableStateOf(false) }

    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Cadence",
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
                            contentDescription = "Ver referencia de colores",
                            tint = ProfileColors.SecondaryText
                        )
                    }

                    DropdownMenu(
                        expanded = showLegend,
                        onDismissRequest = { showLegend = false },
                        modifier = Modifier.background(ProfileColors.Surface)
                    ) {
                        HeatmapLegendEntry(
                            color = heatColor(0f),
                            label = "No leiste"
                        )
                        HeatmapLegendEntry(
                            color = heatColor(0.25f),
                            label = "Mucho menos que el objetivo"
                        )
                        HeatmapLegendEntry(
                            color = heatColor(0.5f),
                            label = "Casi llegas al objetivo"
                        )
                        HeatmapLegendEntry(
                            color = heatColor(0.7f),
                            label = "Objetivo cumplido"
                        )
                        HeatmapLegendEntry(
                            color = heatColor(0.92f),
                            label = "Un poco o mucho mas del objetivo"
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF9F8F6))
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
                            values.forEach { week ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    week.forEach { intensity ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(12.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(heatColor(intensity))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun heatColor(intensity: Float): Color {
    val clamped = intensity.coerceIn(0f, 1f)
    return when {
        clamped < 0.12f -> Color(0xFFF3EFEB)
        clamped < 0.35f -> Color(0xFFE0D0BC)
        clamped < 0.60f -> Color(0xFFC5A583)
        clamped < 0.82f -> Color(0xFF7B8EA3)
        else -> Color(0xFF32475E)
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
