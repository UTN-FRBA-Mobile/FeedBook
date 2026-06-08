package com.example.feedbook.features.library.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
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
    onLogoutClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onAuthorClick: (String) -> Unit = {}
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
        onNotificationsClick = onNotificationsClick,
        onLogoutClick = onLogoutClick
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ProfileColors.Background)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading library...", color = ProfileColors.PrimaryText)
            }
        } else if (state.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ProfileColors.Background)
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.errorMessage, color = ProfileColors.PrimaryText)
            }
        } else {
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
                            onBookClick = { bookId -> onBookClick(bookId) }
                        )
                    }
                    item {
                        LibraryCollectionCard(
                            books = state.readingBooks,
                            title = "READING",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onBookClick = onBookClick
                        )
                    }
                    item {
                        LibraryCollectionCard(
                            books = state.shelfBooks,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onBookClick = onBookClick
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
                if (state.followedAuthors.isNotEmpty()) {
                    item {
                        FollowedAuthorsSection(
                            authors = state.followedAuthors,
                            onAuthorClick = onAuthorClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FollowedAuthorsSection(
    modifier: Modifier = Modifier,
    authors: List<FollowedAuthorUiModel>,
    onAuthorClick: (String) -> Unit
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column {
            Text(
                text = "AUTORES QUE SIGUES",
                style = ProfileTypography.LabelUppercase,
                color = ProfileColors.PrimaryText,
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 4.dp)
            ) {
                items(authors, key = { it.id }) { author ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(72.dp)
                            .clickable { onAuthorClick(author.id) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ProfileColors.AccentSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!author.imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = author.imageUrl,
                                    contentDescription = author.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Text(
                                    text = author.name.first().toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = ProfileColors.PrimaryText
                                )
                            }
                        }
                        Text(
                            text = author.name,
                            style = ProfileTypography.Body.copy(fontSize = 11.sp),
                            color = ProfileColors.PrimaryText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
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
