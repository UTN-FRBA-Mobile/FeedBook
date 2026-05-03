package com.example.feedbook.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.presentation.profile.components.CurrentlyReadingCard
import com.example.feedbook.presentation.profile.components.FeaturedReviewCard
import com.example.feedbook.presentation.profile.components.LibraryArchiveCard
import com.example.feedbook.presentation.profile.components.BottomBarTab
import com.example.feedbook.presentation.profile.components.ProfileBottomBar
import com.example.feedbook.presentation.profile.components.ProfileColors
import com.example.feedbook.presentation.profile.components.ProfileHeaderSection
import com.example.feedbook.presentation.profile.components.ProfileTopBar
import com.example.feedbook.presentation.profile.components.PublicLibraryCard
import com.example.feedbook.presentation.profile.components.PublicProfileStatsCard
import com.example.feedbook.presentation.profile.components.ReadingGoalCard
import com.example.feedbook.presentation.profile.components.ReadingStreakCard
import com.example.feedbook.presentation.profile.components.UpNextCard

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileUiState = sampleProfileUiState(),
    onProfileClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onPreviewPublicProfileClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ProfileColors.Background,
        topBar = {
            ProfileTopBar(
                variant = state.variant,
                avatarStyle = state.avatarStyle,
                avatarImageUri = state.avatarImageUri,
                onAvatarClick = onProfileClick
            )
        },
        bottomBar = {
            ProfileBottomBar(
                activeTab = BottomBarTab.FEED,
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
                        actionLabel = state.actionLabel,
                        avatarStyle = state.avatarStyle,
                        avatarImageUri = state.avatarImageUri,
                        onActionClick = {
                            if (state.variant == ProfileVariant.OWN) onEditProfileClick()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    if (state.variant == ProfileVariant.OWN) {
                        TextButton(onClick = onPreviewPublicProfileClick) {
                            Text(
                                text = "Preview Public View",
                                style = com.example.feedbook.presentation.profile.components.ProfileTypography.Label,
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
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        UpNextCard(
                            books = state.upNextBooks,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        LibraryArchiveCard(
                            completedBooks = state.completedBooks,
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
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        CurrentlyReadingCard(
                            currentBook = state.currentBook,
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

@Preview(showBackground = true, heightDp = 1500, widthDp = 390)
@Composable
private fun ProfileScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true, heightDp = 1500, widthDp = 390)
@Composable
private fun PublicProfileScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        ProfileScreen(state = samplePublicProfileUiState())
    }
}
