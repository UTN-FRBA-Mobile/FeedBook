package com.example.feedbook.features.home.data.remote.dto

import com.example.feedbook.features.profile.data.remote.dto.AvatarDto

data class HomeDto(
    val trendingTitle: String,
    val avatar: AvatarDto,
    val featuredBook: HomeFeaturedBookDto,
    val rankedBooks: List<HomeRankedBookDto>,
    val readingRooms: List<HomeReadingRoomDto>,
    val curators: List<HomeCuratorDto>
)

data class HomeFeaturedBookDto(
    val label: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeRankedBookDto(
    val bookId: String,
    val rankLabel: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeReadingRoomDto(
    val id: String,
    val hostName: String,
    val hostImageUrl: String?,
    val title: String,
    val shortDescription: String,
    val readerCountLabel: String,
    val memberCount: Int,
    val isFollowed: Boolean,
    val isAdult: Boolean
)

data class HomeCuratorDto(
    val name: String,
    val focus: String,
    val imageUrl: String?
)
