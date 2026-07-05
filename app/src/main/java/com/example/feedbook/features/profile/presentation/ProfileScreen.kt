package com.example.feedbook.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedbook.R
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.ErrorScreen
import com.example.feedbook.core.ui.components.LoadingScreen
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.components.CurrentlyReadingCard
import com.example.feedbook.features.profile.presentation.components.FeaturedReviewCard
import com.example.feedbook.features.profile.presentation.components.LibraryArchiveCard
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileHeaderSection
import com.example.feedbook.features.profile.presentation.components.PublicLibraryCard
import com.example.feedbook.features.profile.presentation.components.PublicProfileStatsCard
import com.example.feedbook.features.profile.presentation.components.ReadingGoalCard
import com.example.feedbook.features.profile.presentation.components.ReadingStreakCard

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileUiState,
    topBarAvatarStyle: AvatarStyle? = null,
    topBarAvatarPreset: AvatarPreset? = null,
    topBarAvatarImageUri: String? = null,
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onCollectionClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onPreviewPublicProfileClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onFollowClick: () -> Unit = {},
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
        variant = state.variant,
        activeTab = BottomBarTab.FEED,
        avatarStyle = topBarAvatarStyle ?: state.avatarStyle,
        avatarPreset = topBarAvatarPreset ?: state.avatarPreset,
        avatarImageUri = topBarAvatarImageUri ?: state.avatarImageUri,
        onAvatarClick = onProfileClick,
        onRefreshClick = onRefreshClick,
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
            contentPadding = PaddingValues(top = 48.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    ProfileHeaderSection(
                        variant = state.variant,
                        name = state.name,
                        handle = state.handle,
                        quote = state.quote,
                        actionLabelRes = state.actionLabelRes,
                        avatarStyle = state.avatarStyle,
                        avatarPreset = state.avatarPreset,
                        avatarImageUri = state.avatarImageUri,
                        isFollowing = state.isFollowing,
                        onActionClick = {
                            when (state.variant) {
                                ProfileVariant.OWN -> onEditProfileClick()
                                ProfileVariant.PUBLIC -> onFollowClick()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    if (state.variant == ProfileVariant.OWN) {
                        TextButton(onClick = onPreviewPublicProfileClick) {
                            Text(
                                text = stringResource(R.string.profile_preview_public_view),
                                style = com.example.feedbook.features.profile.presentation.components.ProfileTypography.Label,
                                color = ProfileColors.SecondaryText
                            )
                        }
                    }
                }
            }
            when (state.variant) {
                ProfileVariant.OWN -> {
                    item {
                        ReadingGoalCard(
                            readingGoal = state.readingGoal,
                            onEditGoalClick = onEditProfileClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        ReadingStreakCard(
                            streak = state.readingStreak,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        CurrentlyReadingCard(
                            currentBook = state.currentBook,
                            onBookClick = onBookClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        LibraryArchiveCard(
                            completedBooks = state.completedBooks,
                            onViewCollectionClick = onCollectionClick,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                ProfileVariant.PUBLIC -> {
                    item {
                        PublicProfileStatsCard(
                            stats = state.profileStats,
                            onFollowersClick = onFollowersClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        CurrentlyReadingCard(
                            currentBook = state.currentBook,
                            onBookClick = onBookClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        PublicLibraryCard(
                            books = state.publicLibrary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        FeaturedReviewCard(
                            reviews = state.featuredReviews,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 1500, widthDp = 390, apiLevel = 36)
@Composable
private fun ProfileScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        ProfileScreen(state = previewOwnProfileUiState())
    }
}

@Preview(showBackground = true, heightDp = 1500, widthDp = 390)
@Composable
private fun PublicProfileScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        ProfileScreen(state = previewPublicProfileUiState())
    }
}
