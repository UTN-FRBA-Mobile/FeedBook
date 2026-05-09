package com.example.feedbook.features.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

internal enum class BottomBarTab {
    FEED,
    EXPLORE,
    LIBRARY,
    STATS,
    NOTIFICATIONS
}

@Composable
internal fun ProfileBottomBar(
    activeTab: BottomBarTab = BottomBarTab.FEED,
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAF9))
    ) {
        val density = LocalDensity.current
        val widthDp = maxWidth
        val barHeight = (widthDp * 0.16f).coerceIn(64.dp, 78.dp)
        val horizontalPadding = (widthDp * 0.075f).coerceIn(20.dp, 34.dp)
        val iconSize = (widthDp * 0.05f).coerceIn(20.dp, 26.dp)
        val indicatorWidth = (iconSize * 0.95f).coerceIn(16.dp, 22.dp)
        val indicatorHeight = with(density) { (iconSize.toPx() * 0.12f).toDp() }.coerceIn(2.dp, 3.dp)
        val touchTargetSize = (widthDp * 0.14f).coerceIn(52.dp, 64.dp)

        Column {
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Rounded.Home,
                    contentDescription = "Feed",
                    active = activeTab == BottomBarTab.FEED,
                    onClick = onProfileClick,
                    iconSize = iconSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    touchTargetSize = touchTargetSize
                )
                BottomNavItem(
                    icon = Icons.Outlined.Search,
                    contentDescription = "Explore",
                    active = activeTab == BottomBarTab.EXPLORE,
                    iconSize = iconSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    touchTargetSize = touchTargetSize
                )
                BottomNavItem(
                    icon = Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = "Library",
                    active = activeTab == BottomBarTab.LIBRARY,
                    onClick = onLibraryClick,
                    iconSize = iconSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    touchTargetSize = touchTargetSize
                )
                BottomNavItem(
                    icon = Icons.Outlined.BarChart,
                    contentDescription = "Stats",
                    active = activeTab == BottomBarTab.STATS,
                    onClick = onStatsClick,
                    iconSize = iconSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    touchTargetSize = touchTargetSize
                )
                BottomNavItem(
                    icon = Icons.Outlined.NotificationsNone,
                    contentDescription = "Notifications",
                    active = activeTab == BottomBarTab.NOTIFICATIONS,
                    onClick = onNotificationsClick,
                    iconSize = iconSize,
                    indicatorWidth = indicatorWidth,
                    indicatorHeight = indicatorHeight,
                    touchTargetSize = touchTargetSize
                )
            }
            Box(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    contentDescription: String,
    active: Boolean = false,
    onClick: () -> Unit = {},
    iconSize: androidx.compose.ui.unit.Dp = 18.dp,
    indicatorWidth: androidx.compose.ui.unit.Dp = 16.dp,
    indicatorHeight: androidx.compose.ui.unit.Dp = 2.dp,
    touchTargetSize: androidx.compose.ui.unit.Dp = 52.dp
) {
    Column(
        modifier = Modifier
            .sizeIn(minWidth = touchTargetSize, minHeight = touchTargetSize)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(1.dp))
                .background(if (active) Color(0xFF0F172A) else Color.Transparent)
                .then(
                    Modifier.size(
                        width = indicatorWidth,
                        height = indicatorHeight
                    )
                )
        )
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = if (active) Color(0xFF0F172A) else Color(0xFF64748B)
        )
    }
}
