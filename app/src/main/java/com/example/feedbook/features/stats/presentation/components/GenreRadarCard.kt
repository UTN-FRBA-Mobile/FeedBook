package com.example.feedbook.features.stats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalDensity
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import com.example.feedbook.features.stats.presentation.RadarAxis
import com.example.feedbook.features.stats.presentation.RankingItem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
internal fun GenreRadarCard(
    modes: List<String>,
    selectedMode: String,
    axes: List<RadarAxis>,
    ranking: List<RankingItem>,
    onModeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ProfileColors.AccentSoft)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                modes.forEach { mode ->
                    val selected = mode == selectedMode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selected) ProfileColors.SurfaceStrong else Color.Transparent)
                            .clickable { onModeSelected(mode) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode,
                            style = ProfileTypography.Label,
                            color = if (selected) Color.White else ProfileColors.SecondaryText
                        )
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                val density = LocalDensity.current
                val widthPx = with(density) { maxWidth.toPx() }
                val heightPx = with(density) { maxHeight.toPx() }
                val radius = min(widthPx, heightPx) * 0.24f
                val center = Offset(widthPx / 2f, heightPx / 2.15f)
                val stepAngle = (2 * PI / axes.size).toFloat()
                val labelRadius = radius + with(density) { 42.dp.toPx() }
                val labelWidthPx = with(density) { 82.dp.toPx() }
                val labelHeightPx = with(density) { 34.dp.toPx() }

                Canvas(modifier = Modifier.matchParentSize()) {
                    val levels = 4

                    repeat(levels) { level ->
                        val ratio = (level + 1) / levels.toFloat()
                        val path = Path()
                        axes.indices.forEach { index ->
                            val angle = -PI.toFloat() / 2f + index * stepAngle
                            val point = Offset(
                                x = center.x + cos(angle) * radius * ratio,
                                y = center.y + sin(angle) * radius * ratio
                            )
                            if (index == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
                        }
                        path.close()
                        drawPath(
                            path = path,
                            color = ProfileColors.Divider,
                            style = Stroke(width = 1f)
                        )
                    }

                    axes.indices.forEach { index ->
                        val angle = -PI.toFloat() / 2f + index * stepAngle
                        val outer = Offset(
                            x = center.x + cos(angle) * radius,
                            y = center.y + sin(angle) * radius
                        )
                        drawLine(
                            color = ProfileColors.Divider,
                            start = center,
                            end = outer,
                            strokeWidth = 1f,
                            cap = StrokeCap.Round
                        )
                    }

                    val areaPath = Path()
                    axes.forEachIndexed { index, axis ->
                        val angle = -PI.toFloat() / 2f + index * stepAngle
                        val point = Offset(
                            x = center.x + cos(angle) * radius * axis.value.coerceIn(0f, 1f),
                            y = center.y + sin(angle) * radius * axis.value.coerceIn(0f, 1f)
                        )
                        if (index == 0) areaPath.moveTo(point.x, point.y) else areaPath.lineTo(point.x, point.y)
                    }
                    areaPath.close()

                    drawPath(
                        path = areaPath,
                        color = ProfileColors.SurfaceStrong.copy(alpha = 0.28f)
                    )
                    drawPath(
                        path = areaPath,
                        color = ProfileColors.SurfaceStrong,
                        style = Stroke(width = 3f)
                    )
                }

                axes.forEachIndexed { index, axis ->
                    val angle = -PI.toFloat() / 2f + index * stepAngle
                    val anchor = Offset(
                        x = center.x + cos(angle) * labelRadius,
                        y = center.y + sin(angle) * labelRadius
                    )
                    val x = (anchor.x - labelWidthPx / 2f)
                        .coerceIn(0f, widthPx - labelWidthPx)
                    val y = (anchor.y - labelHeightPx / 2f)
                        .coerceIn(0f, heightPx - labelHeightPx)

                    Text(
                        text = axis.label,
                        style = ProfileTypography.Label.copy(fontSize = 10.sp, lineHeight = 11.sp),
                        color = ProfileColors.SecondaryText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .width(82.dp)
                            .offset {
                                IntOffset(
                                    x = x.toInt(),
                                    y = y.toInt()
                                )
                            }
                    )
                }
            }

            HorizontalDivider(color = ProfileColors.Divider)

            val rankingScrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 188.dp)
                    .verticalScroll(rankingScrollState),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ranking.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "#${item.rank}",
                            style = ProfileTypography.Label.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = ProfileColors.Accent
                            ),
                            color = ProfileColors.Accent
                        )
                        Text(
                            text = item.label,
                            style = ProfileTypography.Body.copy(fontSize = 15.sp),
                            color = ProfileColors.PrimaryText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (item != ranking.last()) {
                        HorizontalDivider(color = ProfileColors.Divider)
                    }
                }
            }
        }
    }
}
