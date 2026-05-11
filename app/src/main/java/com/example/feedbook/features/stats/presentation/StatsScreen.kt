package com.example.feedbook.features.stats.presentation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.ErrorScreen
import com.example.feedbook.core.ui.components.LoadingScreen
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import com.example.feedbook.features.stats.presentation.components.GenreRadarCard
import com.example.feedbook.features.stats.presentation.components.ReadingHeatmapCard
import com.example.feedbook.features.stats.presentation.components.StatsMetricCard

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    state: StatsUiState,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onModeSelected: (String) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    val currentRadarSection = state.radarSections.firstOrNull { it.mode == state.selectedRadarMode }
        ?: state.radarSections.firstOrNull()

    when {
        state.isLoading -> {
            LoadingScreen(modifier = modifier)
            return
        }
        state.errorMessage != null -> {
            ErrorScreen(
                message = state.errorMessage ?: stringResource(R.string.common_error_generic),
                modifier = modifier,
                retryLabel = stringResource(R.string.common_retry),
                onRetry = onRetry
            )
            return
        }
    }

    FeedBookScreenScaffold(
        modifier = modifier.fillMaxSize(),
        variant = ProfileVariant.OWN,
        activeTab = BottomBarTab.STATS,
        avatarStyle = state.avatarStyle,
        avatarPreset = state.avatarPreset,
        avatarImageUri = state.avatarImageUri,
        onAvatarClick = onProfileClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick
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
                    scale = state.heatmapScale,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            currentRadarSection?.let { radarSection ->
                item {
                    GenreRadarCard(
                        modes = state.radarSections.map { it.mode },
                        selectedMode = state.selectedRadarMode,
                        axes = radarSection.axes,
                        ranking = radarSection.ranking,
                        onModeSelected = onModeSelected,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun StatsScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        StatsScreen(state = previewStatsUiState())
    }
}
