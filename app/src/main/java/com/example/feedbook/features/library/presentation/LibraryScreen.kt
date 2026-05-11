package com.example.feedbook.features.library.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.library.presentation.components.LibraryCollectionCard
import com.example.feedbook.features.library.presentation.components.LibraryOverviewCard
import com.example.feedbook.features.library.presentation.components.ReadHistoryCard
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.components.CurrentlyReadingCard
import com.example.feedbook.features.profile.presentation.components.LibraryArchiveCard
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileTypography

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    state: LibraryUiState = sampleLibraryUiState(),
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {}
) {
    var showReadCollection by rememberSaveable { mutableStateOf(false) }

    FeedBookScreenScaffold(
        modifier = modifier.fillMaxSize(),
        variant = ProfileVariant.OWN,
        activeTab = BottomBarTab.LIBRARY,
        avatarStyle = state.avatarStyle,
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
                    style = ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 22.sp),
                    color = ProfileColors.SecondaryText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                LibraryOverviewCard(
                    readingCount = state.readingCount,
                    shelfCount = state.shelfCount,
                    completedBooks = state.completedBooks,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            if (!showReadCollection) {
                item {
                    CurrentlyReadingCard(
                        currentBook = state.currentBook,
                        emphasized = true,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onBookClick = { bookId -> onBookClick(bookId)}
                    )
                }
                item {
                    LibraryCollectionCard(
                        books = state.readingBooks,
                        title = "READING",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    LibraryCollectionCard(
                        books = state.shelfBooks,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    LibraryArchiveCard(
                        completedBooks = state.completedBooks,
                        onViewCollectionClick = { showReadCollection = true },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            } else {
                item {
                    Text(
                        text = "Back to library",
                        style = ProfileTypography.LabelUppercase,
                        color = ProfileColors.Accent,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { showReadCollection = false }
                    )
                }
                item {
                    ReadHistoryCard(
                        books = state.readHistory,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun LibraryScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        LibraryScreen()
    }
}
