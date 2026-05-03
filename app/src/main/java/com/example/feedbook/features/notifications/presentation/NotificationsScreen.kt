package com.example.feedbook.features.notifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.components.BottomBarTab
import com.example.feedbook.features.profile.presentation.components.ProfileBottomBar
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTopBar
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    state: NotificationsUiState = sampleNotificationsUiState(),
    onProfileClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ProfileColors.Background,
        topBar = {
            ProfileTopBar(
                variant = com.example.feedbook.features.profile.presentation.ProfileVariant.OWN,
                avatarStyle = state.avatarStyle,
                avatarImageUri = state.avatarImageUri,
                onAvatarClick = onProfileClick,
                trailingContent = { iconSize ->
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notifications",
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
                activeTab = BottomBarTab.NOTIFICATIONS,
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = state.title,
                    style = ProfileTypography.Label.copy(fontSize = 13.sp, lineHeight = 18.sp),
                    color = ProfileColors.SecondaryText
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.items.forEach { item ->
                        when (item) {
                            is NotificationItem.FriendActivity -> FriendActivityCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendActivityCard(item: NotificationItem.FriendActivity) {
    ProfileSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(item.avatarTopColor, item.avatarBottomColor)
                            )
                        )
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.message,
                        style = ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 20.sp),
                        color = ProfileColors.SecondaryText,
                        maxLines = if (item.bookPreview == null) 4 else 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = item.timestamp,
                        style = ProfileTypography.LabelUppercase.copy(fontSize = 9.sp, lineHeight = 10.sp),
                        color = Color(0xFF8B8B8B)
                    )
                }

                if (item.badge != null) {
                    Text(
                        text = item.badge,
                        style = ProfileTypography.LabelUppercase,
                        color = ProfileColors.Accent
                    )
                }
            }

            if (item.bookPreview != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                        .border(1.dp, Color(0xFFE8E3DE), androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 34.dp, height = 52.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                            .background(item.bookPreview.accent)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = item.bookPreview.title,
                            style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 18.sp),
                            color = ProfileColors.PrimaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.bookPreview.author,
                            style = ProfileTypography.LabelUppercase.copy(fontSize = 8.sp, lineHeight = 10.sp),
                            color = ProfileColors.SecondaryText
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun NotificationsScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        NotificationsScreen()
    }
}
