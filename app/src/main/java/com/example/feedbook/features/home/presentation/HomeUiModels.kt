package com.example.feedbook.features.home.presentation

import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class HomeUiState(
    val trendingTitle: String,
    val avatarStyle: AvatarStyle,
    val avatarPreset: AvatarPreset?,
    val avatarImageUri: String?,
    val featuredBook: HomeFeaturedBookUi,
    val rankedBooks: List<HomeRankedBookUi>,
    val readingRooms: List<HomeReadingRoomUi>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class HomeFeaturedBookUi(
    val label: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeRankedBookUi(
    val bookId: String,
    val rankLabel: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeReadingRoomUi(
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

fun sampleHomeUiState(): HomeUiState = HomeUiState(
    trendingTitle = "Trending Now",
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null,
    featuredBook = HomeFeaturedBookUi(
        label = "FEATURED",
        title = "The Name of the Wind",
        author = "Patrick Rothfuss",
        coverImageUrl = "https://covers.openlibrary.org/b/isbn/9780756404741-L.jpg"
    ),
    rankedBooks = listOf(
        HomeRankedBookUi("circe", "01", "Circe", "Madeline Miller", "https://covers.openlibrary.org/b/isbn/9780316556323-L.jpg"),
        HomeRankedBookUi("piranesi", "02", "Piranesi", "Susanna Clarke", "https://covers.openlibrary.org/b/isbn/9781635575637-L.jpg"),
        HomeRankedBookUi("project-hail-mary", "03", "Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg")
    ),
    readingRooms = listOf(
        HomeReadingRoomUi(
            id = "room-magical-realism",
            hostName = "Eleanor",
            hostImageUrl = null,
            title = "Magical Realism Book Club",
            shortDescription = "Realismo magico y debates lentos.",
            readerCountLabel = "1.2k readers",
            memberCount = 1200,
            isFollowed = true,
            isAdult = false
        ),
        HomeReadingRoomUi(
            id = "room-classics",
            hostName = "James",
            hostImageUrl = null,
            title = "20th Century Classics",
            shortDescription = "Clasicos modernos con contexto.",
            readerCountLabel = "850 readers",
            memberCount = 850,
            isFollowed = false,
            isAdult = false
        )
    ),
    isLoading = false,
    errorMessage = null
)

fun emptyHomeUiState(): HomeUiState = HomeUiState(
    trendingTitle = "",
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null,
    featuredBook = HomeFeaturedBookUi("", "", "", null),
    rankedBooks = emptyList(),
    readingRooms = emptyList(),
    isLoading = false,
    errorMessage = null
)
