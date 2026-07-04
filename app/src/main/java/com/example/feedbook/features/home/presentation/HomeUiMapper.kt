package com.example.feedbook.features.home.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.home.domain.model.HomeFeed
import com.example.feedbook.features.profile.presentation.AvatarStyle

fun HomeFeed.toUiState(): HomeUiState {
    val avatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )
    return HomeUiState(
        trendingTitle = trendingTitle,
        avatarStyle = avatarStyle,
        avatarPreset = null,
        avatarImageUri = avatar.imageUri,
        featuredBook = HomeFeaturedBookUi(
            bookId = featuredBook.bookId,
            label = featuredBook.label,
            title = featuredBook.title,
            author = featuredBook.author,
            coverImageUrl = featuredBook.coverImageUrl
        ),
        rankedBooks = rankedBooks.map {
            HomeRankedBookUi(
                bookId = it.bookId,
                rankLabel = it.rankLabel,
                title = it.title,
                author = it.author,
                coverImageUrl = it.coverImageUrl
            )
        },
        readingRooms = readingRooms.map {
            HomeReadingRoomUi(
                id = it.id,
                hostName = it.hostName,
                hostImageUrl = it.hostImageUrl,
                title = it.title,
                shortDescription = it.shortDescription,
                readerCountLabel = it.readerCountLabel,
                memberCount = it.memberCount,
                isFollowed = it.isFollowed,
                isAdult = it.isAdult
            )
        },
        isLoading = false,
        errorMessage = null
    )
}
