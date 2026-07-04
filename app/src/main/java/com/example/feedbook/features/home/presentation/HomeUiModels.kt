package com.example.feedbook.features.home.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle

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

data class HomeCuratorUi(
    val name: String,
    val focus: String,
    val imageUrl: String?
)

fun sampleHomeUiState(): HomeUiState = HomeUiState(
    trendingTitle = "Trending Now",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarPreset = null,
    avatarImageUri = null,
    featuredBook = HomeFeaturedBookUi(
        label = "FEATURED",
        title = "The Midnight Library",
        author = "Matt Haig",
        coverImageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=1200&q=80"
    ),
    rankedBooks = listOf(
        HomeRankedBookUi("01", "Circe", "Madeline Miller", "https://covers.openlibrary.org/b/isbn/9780316556323-L.jpg"),
        HomeRankedBookUi("02", "Piranesi", "Susanna Clarke", "https://covers.openlibrary.org/b/isbn/9781635575637-L.jpg"),
        HomeRankedBookUi("03", "Project Hail Mary", "Andy Weir", "https://covers.openlibrary.org/b/isbn/9780593135204-L.jpg")
    ),
    readingRooms = listOf(
        HomeReadingRoomUi(
            id = "room-magical-realism",
            hostName = "Eleanor",
            hostImageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=eleanor&size=128",
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
            hostImageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=james&size=128",
            title = "20th Century Classics",
            shortDescription = "Clasicos modernos con contexto.",
            readerCountLabel = "850 readers",
            memberCount = 850,
            isFollowed = false,
            isAdult = false
        )
    ),
    curators = listOf(
        HomeCuratorUi(
            name = "Dr. Aris Thorne",
            focus = "Historical Non-Fiction Focus",
            imageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=aris-thorne&size=128"
        ),
        HomeCuratorUi(
            name = "Lila Vance",
            focus = "Contemporary Lit & Essays",
            imageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=lila-vance&size=128"
        )
    ),
    isLoading = false,
    errorMessage = null
)

fun emptyHomeUiState(): HomeUiState = HomeUiState(
    trendingTitle = "",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarPreset = null,
    avatarImageUri = null,
    featuredBook = HomeFeaturedBookUi("", "", "", null),
    rankedBooks = emptyList(),
    readingRooms = emptyList(),
    curators = emptyList(),
    isLoading = false,
    errorMessage = null
)
