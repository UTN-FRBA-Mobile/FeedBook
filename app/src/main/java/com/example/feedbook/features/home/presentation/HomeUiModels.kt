package com.example.feedbook.features.home.presentation

import com.example.feedbook.features.profile.presentation.AvatarStyle
import androidx.compose.ui.graphics.Color

data class HomeUiState(
    val trendingTitle: String,
    val avatarStyle: AvatarStyle,
    val avatarImageUri: String?,
    val featuredBook: HomeFeaturedBookUi,
    val rankedBooks: List<HomeRankedBookUi>,
    val readingRooms: List<HomeReadingRoomUi>,
    val curators: List<HomeCuratorUi>
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
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
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
            hostName = "Eleanor",
            hostImageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=eleanor&size=128",
            title = "Magical Realism Book Club",
            readerCountLabel = "1.2k readers"
        ),
        HomeReadingRoomUi(
            hostName = "James",
            hostImageUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=james&size=128",
            title = "20th Century Classics",
            readerCountLabel = "850 readers"
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
    )
)
