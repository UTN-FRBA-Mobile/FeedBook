package com.example.feedbook.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.presentation.profile.ProfileVariant
import com.example.feedbook.presentation.profile.components.BottomBarTab
import com.example.feedbook.presentation.profile.components.ProfileBottomBar
import com.example.feedbook.presentation.profile.components.ProfileColors
import com.example.feedbook.presentation.profile.components.ProfileTopBar
import com.example.feedbook.presentation.profile.components.ProfileTypography
import com.example.feedbook.presentation.stats.components.GenreRadarCard
import com.example.feedbook.presentation.stats.components.ReadingHeatmapCard
import com.example.feedbook.presentation.stats.components.StatsMetricCard

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    state: StatsUiState = sampleStatsUiState(),
    avatarStyle: com.example.feedbook.presentation.profile.AvatarStyle = com.example.feedbook.presentation.profile.AvatarStyle(
        topColor = androidx.compose.ui.graphics.Color(0xFF315A73),
        bottomColor = androidx.compose.ui.graphics.Color(0xFFF0C6A8)
    ),
    avatarImageUri: String? = null,
    onProfileClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    var selectedRadarMode by rememberSaveable { mutableStateOf(state.selectedRadarMode) }
    val currentRadarSection = state.radarSections.firstOrNull { it.mode == selectedRadarMode }
        ?: state.radarSections.first()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ProfileColors.Background,
        topBar = {
            ProfileTopBar(
                variant = ProfileVariant.OWN,
                avatarStyle = avatarStyle,
                avatarImageUri = avatarImageUri,
                onAvatarClick = onProfileClick,
                trailingContent = { iconSize ->
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = "Stats",
                        tint = ProfileColors.SecondaryText,
                        modifier = Modifier.size(iconSize)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = ProfileColors.SecondaryText,
                        modifier = Modifier.size(iconSize)
                    )
                }
            )
        },
        bottomBar = {
            ProfileBottomBar(
                activeTab = BottomBarTab.STATS,
                onProfileClick = onProfileClick,
                onStatsClick = onStatsClick,
                onNotificationsClick = onNotificationsClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileColors.Background)
                .padding(innerPadding),
            contentPadding = PaddingValues(top = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = state.title,
                    style = ProfileTypography.HeroName.copy(fontSize = 28.sp, lineHeight = 32.sp),
                    color = ProfileColors.PrimaryText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Text(
                    text = state.subtitle,
                    style = ProfileTypography.Label.copy(fontSize = 11.sp, lineHeight = 16.sp),
                    color = ProfileColors.SecondaryText,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.9f)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.metrics.chunked(2).forEach { rowMetrics ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowMetrics.forEach { metric ->
                                StatsMetricCard(
                                    metric = metric,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowMetrics.size == 1) {
                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                ReadingHeatmapCard(
                    months = state.heatmapMonths,
                    rows = state.heatmapRows,
                    values = state.heatmapValues,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                GenreRadarCard(
                    modes = state.radarSections.map { it.mode },
                    selectedMode = selectedRadarMode,
                    axes = currentRadarSection.axes,
                    ranking = currentRadarSection.ranking,
                    onModeSelected = { selectedRadarMode = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun StatsScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        StatsScreen()
    }
}
