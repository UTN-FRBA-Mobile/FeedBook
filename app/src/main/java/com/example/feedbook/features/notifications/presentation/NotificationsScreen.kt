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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.ErrorScreen
import com.example.feedbook.core.ui.components.LoadingScreen
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    state: NotificationsUiState,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
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
        variant = com.example.feedbook.features.profile.presentation.ProfileVariant.OWN,
        activeTab = BottomBarTab.NOTIFICATIONS,
        avatarStyle = state.avatarStyle,
        avatarPreset = state.avatarPreset,
        avatarImageUri = state.avatarImageUri,
        onAvatarClick = onProfileClick,
        onFeedClick = onFeedClick,
        onExploreClick = onExploreClick,
        onLibraryClick = onLibraryClick,
        onStatsClick = onStatsClick,
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick
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
                            is NotificationItem.FollowedYou -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = stringResource(
                                    R.string.notification_followed_you,
                                    item.actor.name
                                )
                            )
                            is NotificationItem.StartedReading -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = stringResource(
                                    R.string.notification_started_reading,
                                    item.actor.name
                                ),
                                book = item.book
                            )
                            is NotificationItem.ReviewedBook -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = stringResource(
                                    R.string.notification_reviewed_book,
                                    item.actor.name
                                ),
                                book = item.book
                            )
                            is NotificationItem.LikedYourReview -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = stringResource(
                                    R.string.notification_liked_your_review,
                                    item.actor.name
                                )
                            )
                            is NotificationItem.SavedYourBook -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = stringResource(
                                    R.string.notification_saved_your_book,
                                    item.actor.name
                                ),
                                book = item.book
                            )
                            is NotificationItem.Generic -> NotificationCard(
                                actor = item.actor,
                                timestamp = item.timestamp,
                                message = item.fallbackText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    actor: NotificationActorUi,
    timestamp: String,
    message: String,
    book: NotificationBookUi? = null
) {
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
                                listOf(actor.avatarTopColor, actor.avatarBottomColor)
                            )
                        )
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = message,
                        style = ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 20.sp),
                        color = ProfileColors.SecondaryText,
                        maxLines = if (book == null) 4 else 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = timestamp,
                        style = ProfileTypography.LabelUppercase.copy(fontSize = 9.sp, lineHeight = 10.sp),
                        color = Color(0xFF8B8B8B)
                    )
                }
            }

            if (book != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                        .border(1.dp, Color(0xFFE8E3DE), androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RemoteBookCover(
                        title = book.title,
                        coverImageUrl = book.coverImageUrl,
                        modifier = Modifier
                            .size(width = 34.dp, height = 52.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = book.title,
                            style = ProfileTypography.Body.copy(fontSize = 13.sp, lineHeight = 18.sp),
                            color = ProfileColors.PrimaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = book.author,
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
        NotificationsScreen(state = previewNotificationsUiState())
    }
}
