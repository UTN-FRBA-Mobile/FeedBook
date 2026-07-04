package com.example.feedbook.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feedbook.core.ui.components.BottomBarTab
import com.example.feedbook.core.ui.components.FeedBookScreenScaffold
import com.example.feedbook.core.ui.components.RemoteBookCover
import com.example.feedbook.core.ui.theme.FeedBookTheme
import com.example.feedbook.features.profile.presentation.ProfileVariant
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.components.ProfileAvatarArtwork
import com.example.feedbook.features.profile.presentation.components.ProfileColors
import com.example.feedbook.features.profile.presentation.components.ProfileSurfaceCard
import com.example.feedbook.features.profile.presentation.components.ProfileTypography
import androidx.compose.material3.HorizontalDivider

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState = sampleHomeUiState(),
    onFeedClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onStatsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onReadingRoomClick: (String) -> Unit = {},
    onSeeAllReadingRoomsClick: () -> Unit = {}
) {
    FeedBookScreenScaffold(
        modifier = modifier.fillMaxSize(),
        variant = ProfileVariant.OWN,
        activeTab = BottomBarTab.FEED,
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
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ProfileColors.Background)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading home feed...", color = ProfileColors.PrimaryText)
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
                contentPadding = PaddingValues(top = 36.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(34.dp)
            ) {
                item {
                    TrendingHeader(
                        title = state.trendingTitle,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    FeaturedBookCard(
                        book = state.featuredBook,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    RankedBooksSection(
                        books = state.rankedBooks,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    ReadingRoomsSection(
                        rooms = state.readingRooms,
                        modifier = Modifier.fillMaxWidth(),
                        onRoomClick = onReadingRoomClick,
                        onSeeAllClick = onSeeAllReadingRoomsClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = ProfileTypography.HeroName.copy(
                fontSize = 31.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Normal
            ),
            color = ProfileColors.PrimaryText
        )
        Icon(
            imageVector = Icons.Outlined.LocalFireDepartment,
            contentDescription = null,
            tint = ProfileColors.Accent,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun FeaturedBookCard(
    book: HomeFeaturedBookUi,
    modifier: Modifier = Modifier
) {
    ProfileSurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            RemoteBookCover(
                title = book.title,
                coverImageUrl = book.coverImageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(384.dp)
                    .clip(RoundedCornerShape(4.dp)),
                fallbackBackground = Color(0xFF0E1820)
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = book.label,
                    style = ProfileTypography.LabelUppercase.copy(fontSize = 11.sp),
                    color = ProfileColors.Accent
                )
                Text(
                    text = book.title,
                    style = ProfileTypography.HeroName.copy(
                        fontSize = 34.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = book.author,
                    style = ProfileTypography.Body.copy(fontSize = 18.sp, lineHeight = 28.sp),
                    color = ProfileColors.SecondaryText
                )
            }
        }
    }
}

@Composable
private fun RankedBooksSection(
    books: List<HomeRankedBookUi>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Top 3 this week",
                    style = ProfileTypography.HeroName.copy(
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = ProfileColors.PrimaryText
                )
                Text(
                    text = "Most read on FeedBook",
                    style = ProfileTypography.Body.copy(fontSize = 14.sp, lineHeight = 18.sp),
                    color = ProfileColors.SecondaryText
                )
            }
        }
        books.forEachIndexed { index, book ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.rankLabel,
                    style = ProfileTypography.HeroName.copy(
                        fontSize = 28.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = ProfileColors.SecondaryText,
                    modifier = Modifier.width(64.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                RemoteBookCover(
                    title = book.title,
                    coverImageUrl = book.coverImageUrl,
                    modifier = Modifier
                        .size(width = 96.dp, height = 128.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    fallbackBackground = ProfileColors.AccentSoft
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = book.title,
                        style = ProfileTypography.HeroName.copy(
                            fontSize = 28.sp,
                            lineHeight = 32.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = ProfileColors.PrimaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.author,
                        style = ProfileTypography.Body.copy(fontSize = 18.sp, lineHeight = 28.sp),
                        color = ProfileColors.SecondaryText
                    )
                }
            }
            if (index != books.lastIndex) {
                HorizontalDivider(color = ProfileColors.Divider)
            }
        }
    }
}

@Composable
private fun ReadingRoomsSection(
    rooms: List<HomeReadingRoomUi>,
    modifier: Modifier = Modifier,
    onRoomClick: (String) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Reading Rooms",
                style = ProfileTypography.HeroName.copy(
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = ProfileColors.PrimaryText
            )
            Text(
                text = "See all",
                style = ProfileTypography.Body.copy(fontSize = 16.sp, lineHeight = 20.sp),
                color = ProfileColors.SecondaryText,
                modifier = Modifier.clickable(onClick = onSeeAllClick)
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(rooms) { room ->
                ReadingRoomCard(room = room, onClick = { onRoomClick(room.id) })
            }
        }
    }
}

@Composable
private fun ReadingRoomCard(room: HomeReadingRoomUi, onClick: () -> Unit) {
    ProfileSurfaceCard(
        modifier = Modifier
            .width(400.dp)
            .border(
                width = if (room.isFollowed) 2.dp else 1.dp,
                color = if (room.isFollowed) ProfileColors.Accent else ProfileColors.Border,
                shape = RoundedCornerShape(8.dp)
            ),
        onClick = onClick,
        containerColor = ProfileColors.Surface
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileAvatarArtwork(
                    avatarStyle = defaultAvatarStyle(),
                    avatarPreset = null,
                    avatarImageUri = room.hostImageUrl,
                    modifier = Modifier.size(46.dp),
                    imageShape = CircleShape
                )
                Text(
                    text = "Hosted by ${room.hostName}",
                    style = ProfileTypography.Body.copy(fontSize = 16.sp, lineHeight = 22.sp),
                    color = ProfileColors.SecondaryText
                )
            }
            Text(
                text = room.title,
                style = ProfileTypography.HeroName.copy(
                    fontSize = 30.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = ProfileColors.PrimaryText
            )
            Text(
                text = room.shortDescription,
                style = ProfileTypography.Body.copy(fontSize = 16.sp, lineHeight = 22.sp),
                color = ProfileColors.SecondaryText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PeopleOutline,
                    contentDescription = null,
                    tint = ProfileColors.SecondaryText,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = room.readerCountLabel,
                    style = ProfileTypography.Body.copy(fontSize = 16.sp, lineHeight = 20.sp),
                    color = ProfileColors.SecondaryText
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1500)
@Composable
private fun HomeScreenPreview() {
    FeedBookTheme(dynamicColor = false) {
        HomeScreen()
    }
}
