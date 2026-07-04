package com.example.feedbook.features.home.domain.model

data class HomeFeed(
    val trendingTitle: String,
    val avatar: HomeAvatar,
    val featuredBook: HomeFeaturedBook,
    val rankedBooks: List<HomeRankedBook>,
    val readingRooms: List<HomeReadingRoom>,
    val curators: List<HomeCurator>
)

data class HomeAvatar(
    val topColorHex: Long,
    val bottomColorHex: Long,
    val imageUri: String?
)

data class HomeFeaturedBook(
    val bookId: String,
    val label: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeRankedBook(
    val bookId: String,
    val rankLabel: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeReadingRoom(
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

data class HomeCurator(
    val name: String,
    val focus: String,
    val imageUrl: String?
)
