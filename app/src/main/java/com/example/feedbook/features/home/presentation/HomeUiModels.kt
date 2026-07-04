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
    val curators: List<HomeCuratorUi>,
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
    val rankLabel: String,
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class HomeReadingRoomUi(
    val hostName: String,
    val hostImageUrl: String?,
    val title: String,
    val readerCountLabel: String
)

data class HomeCuratorUi(
    val name: String,
    val focus: String,
    val imageUrl: String?
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
        HomeRankedBookUi("01", "Circe", "Madeline Miller", "https://covers.openlibrary.org/b/isbn/9780316556323-L.jpg"),
        HomeRankedBookUi("02", "Piranesi", "Susanna Clarke", "https://covers.openlibrary.org/b/isbn/9781635575637-L.jpg"),
        HomeRankedBookUi("03", "Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg")
    ),
    readingRooms = listOf(
        HomeReadingRoomUi(
            hostName = "Eleanor",
            hostImageUrl = null,
            title = "Magical Realism Book Club",
            readerCountLabel = "1.2k readers"
        ),
        HomeReadingRoomUi(
            hostName = "James",
            hostImageUrl = null,
            title = "20th Century Classics",
            readerCountLabel = "850 readers"
        )
    ),
    curators = listOf(
        HomeCuratorUi(
            name = "Dr. Aris Thorne",
            focus = "Historical Non-Fiction Focus",
            imageUrl = null
        ),
        HomeCuratorUi(
            name = "Lila Vance",
            focus = "Contemporary Lit & Essays",
            imageUrl = null
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
    curators = emptyList(),
    isLoading = false,
    errorMessage = null
)
