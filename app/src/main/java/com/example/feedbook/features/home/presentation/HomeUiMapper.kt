package com.example.feedbook.features.home.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.home.domain.model.HomeFeed
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.avatarPresetFromData

fun HomeFeed.toUiState(): HomeUiState {
    val avatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )
    return HomeUiState(
        trendingTitle = trendingTitle,
        avatarStyle = avatarStyle,
        avatarPreset = avatarPresetFromData(avatar.avatarPresetId, avatarStyle, avatar.presetImageUrl),
        avatarImageUri = avatar.imageUri,
        featuredBook = HomeFeaturedBookUi(
            label = featuredBook.label,
            title = featuredBook.title,
            author = featuredBook.author,
            coverImageUrl = featuredBook.coverImageUrl
        ),
        rankedBooks = rankedBooks.map {
            HomeRankedBookUi(
                rankLabel = it.rankLabel,
                title = it.title,
                author = it.author,
                coverImageUrl = it.coverImageUrl
            )
        },
        readingRooms = readingRooms.map {
            HomeReadingRoomUi(
                hostName = it.hostName,
                hostImageUrl = it.hostImageUrl,
                title = it.title,
                readerCountLabel = it.readerCountLabel
            )
        },
        curators = curators.map {
            HomeCuratorUi(
                name = it.name,
                focus = it.focus,
                imageUrl = it.imageUrl
            )
        },
        isLoading = false,
        errorMessage = null
    )
}
