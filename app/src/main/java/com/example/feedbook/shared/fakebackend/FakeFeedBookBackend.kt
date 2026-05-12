package com.example.feedbook.shared.fakebackend

import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationActorDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationEntryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationBookSummaryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationTypes
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.home.data.remote.dto.HomeCuratorDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.home.data.remote.dto.HomeFeaturedBookDto
import com.example.feedbook.features.home.data.remote.dto.HomeRankedBookDto
import com.example.feedbook.features.home.data.remote.dto.HomeReadingRoomDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.library.data.remote.dto.ReadBookDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarPresetDto
import com.example.feedbook.features.profile.data.remote.dto.CurrentBookDto
import com.example.feedbook.features.profile.data.remote.dto.FeaturedReviewDto
import com.example.feedbook.features.profile.data.remote.dto.LibraryBookDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileStatDto
import com.example.feedbook.features.profile.data.remote.dto.QueuedBookDto
import com.example.feedbook.features.profile.data.remote.dto.ReadingGoalDto
import com.example.feedbook.features.profile.data.remote.dto.ReadingStreakDto
import com.example.feedbook.features.profile.data.remote.dto.StreakDayDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.feedbook.features.stats.data.remote.dto.RadarAxisDto
import com.example.feedbook.features.stats.data.remote.dto.RadarSectionDto
import com.example.feedbook.features.stats.data.remote.dto.RankingItemDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import com.example.feedbook.features.stats.data.remote.dto.StatsMetricDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class FakeFeedBookBackend {
    private fun coverUrl(isbn: String): String =
        "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg"

    private fun avatarUrl(seed: String): String =
        "https://api.dicebear.com/9.x/adventurer/png?seed=$seed&size=128"

    private fun avatarPresets(): List<AvatarPresetDto> = listOf(
        AvatarPresetDto("vampire", 0xFF382845, 0xFFBFA7CF, avatarUrl("vampire")),
        AvatarPresetDto("werewolf", 0xFF4A3C32, 0xFFC8AE96, avatarUrl("werewolf")),
        AvatarPresetDto("witch", 0xFF344B39, 0xFFC8D3B5, avatarUrl("witch")),
        AvatarPresetDto("wizard", 0xFF29496B, 0xFFC5D5E8, avatarUrl("wizard")),
        AvatarPresetDto("harry_potter", 0xFF6B2E2A, 0xFFE5C77F, avatarUrl("harry-potter")),
        AvatarPresetDto("astronaut", 0xFF24364D, 0xFFCAD8E7, avatarUrl("astronaut")),
        AvatarPresetDto("grim_reaper", 0xFF2B2B31, 0xFFB8BBC4, avatarUrl("grim-reaper")),
        AvatarPresetDto("fairy", 0xFF5B4A80, 0xFFF0CCE9, avatarUrl("fairy")),
        AvatarPresetDto("pirate", 0xFF5A3527, 0xFFE2C09A, avatarUrl("pirate")),
        AvatarPresetDto("princess", 0xFF9A5C8D, 0xFFF2D8EB, avatarUrl("princess")),
        AvatarPresetDto("king", 0xFF70511F, 0xFFF0D9A0, avatarUrl("king")),
        AvatarPresetDto("ghost", 0xFF5B6775, 0xFFE6EBF0, avatarUrl("ghost"))
    )

    private val ownProfileState = MutableStateFlow(
        ProfileDto(
            name = "Evelyn Vance",
            handle = "@evelynv",
            quote = "\"Reading is a conversation. All books talk. But a good book listens as well.\"",
            avatar = AvatarDto(0xFF5B4A80, 0xFFF0CCE9, "witch", avatarUrl("witch"), null),
            availableAvatarPresets = avatarPresets(),
            readingGoal = ReadingGoalDto(40, 28),
            readingStreak = ReadingStreakDto(
                days = 5,
                week = listOf(
                    StreakDayDto("M", 0.18f, false, false),
                    StreakDayDto("T", 0.72f, false, true),
                    StreakDayDto("W", 1f, false, true),
                    StreakDayDto("T", 0.48f, false, true),
                    StreakDayDto("F", 1f, false, true),
                    StreakDayDto("S", 0.88f, false, true),
                    StreakDayDto("S", 0f, true, false)
                )
            ),
            currentBook = CurrentBookDto(
                id = "1",
                title = "The Secret History",
                author = "Donna Tartt",
                page = 248,
                totalPages = 559,
                progress = 0.44f,
                coverImageUrl = coverUrl("9781400031702")
            ),
            upNextBooks = listOf(
                QueuedBookDto("Foucault's Pendulum", "Umberto Eco", coverUrl("9780156032971")),
                QueuedBookDto("The Shadow of the Wind", "Carlos Ruiz Zafon", coverUrl("9780143034902")),
                QueuedBookDto("If on a winter's night a traveler", "Italo Calvino", coverUrl("9780156439619"))
            ),
            completedBooks = 142,
            profileStats = listOf(
                ProfileStatDto("Books read", "142"),
                ProfileStatDto("This year", "19")
            ),
            publicLibrary = listOf(
                LibraryBookDto("The Secret History", coverUrl("9781400031702")),
                LibraryBookDto("Ficciones", coverUrl("9780802130303")),
                LibraryBookDto("Never Let Me Go", coverUrl("9781400078776")),
                LibraryBookDto("Beloved", coverUrl("9781400033416")),
                LibraryBookDto("Pale Fire", coverUrl("9780679723424")),
                LibraryBookDto("The Waves", coverUrl("9780156949606"))
            ),
            featuredReviews = listOf(
                FeaturedReviewDto(
                    bookTitle = "The Secret History",
                    rating = 5,
                    timeAgo = "2d ago",
                    excerpt = "\"A novel built on obsession, elitism and silence. Tartt makes every scene feel both intimate and dangerous.\"",
                    coverImageUrl = coverUrl("9781400031702")
                ),
                FeaturedReviewDto(
                    bookTitle = "Beloved",
                    rating = 5,
                    timeAgo = "1w ago",
                    excerpt = "\"Morrison writes memory like weather. Every return to this novel feels heavier and more precise.\"",
                    coverImageUrl = coverUrl("9781400033416")
                )
            )
        )
    )

    private val publicProfile = ProfileDto(
        name = "Julian Thorne",
        handle = "@julianthorne",
        quote = "\"I collect stories that feel like half-remembered dreams and impossible cities.\"",
        avatar = AvatarDto(0xFF5A3527, 0xFFE2C09A, "pirate", avatarUrl("pirate"), null),
        availableAvatarPresets = avatarPresets(),
        readingGoal = null,
        readingStreak = ReadingStreakDto(
            days = 0,
            week = listOf(
                StreakDayDto("M", 0f, false, false),
                StreakDayDto("T", 0f, false, false),
                StreakDayDto("W", 0f, false, false),
                StreakDayDto("T", 0f, false, false),
                StreakDayDto("F", 0f, false, false),
                StreakDayDto("S", 0f, false, false),
                StreakDayDto("S", 0f, true, false)
            )
        ),
        currentBook = CurrentBookDto(
            id = "2",
            title = "The Name of the Rose",
            author = "Umberto Eco",
            page = 312,
            totalPages = 512,
            progress = 0.61f,
            coverImageUrl = coverUrl("9780156001311")
        ),
        upNextBooks = emptyList(),
        completedBooks = 58,
        profileStats = listOf(
            ProfileStatDto("Reviews", "128"),
            ProfileStatDto("Followers", "2.4K")
        ),
        publicLibrary = listOf(
            LibraryBookDto("One Hundred Years of Solitude", coverUrl("9780060883287")),
            LibraryBookDto("The Shadow of the Wind", coverUrl("9780143034902")),
            LibraryBookDto("Ficciones", coverUrl("9780802130303")),
            LibraryBookDto("Invisible Cities", coverUrl("9780156453806")),
            LibraryBookDto("Austerlitz", coverUrl("9780811216548")),
            LibraryBookDto("If on a winter's night a traveler", coverUrl("9780156439619")),
            LibraryBookDto("The Left Hand of Darkness", coverUrl("9780441478125")),
            LibraryBookDto("Pedro Paramo", coverUrl("9780802133908")),
            LibraryBookDto("The Master and Margarita", coverUrl("9780143108276"))
        ),
        featuredReviews = listOf(
            FeaturedReviewDto("The Name of the Rose", 5, "4h ago", "\"A profound meditation on destiny. The novel keeps its labyrinth open long after the final page.\"", coverUrl("9780156001311")),
            FeaturedReviewDto("Invisible Cities", 4, "3d ago", "\"Calvino turns urban imagination into something light and exact. Every fragment expands after you finish it.\"", coverUrl("9780156453806")),
            FeaturedReviewDto("Austerlitz", 5, "1w ago", "\"A quiet, relentless novel. Sebald makes memory feel architectural, fragile and impossible to escape.\"", coverUrl("9780811216548"))
        )
    )

    private val stats = StatsDto(
        title = "Reading Ledger",
        subtitle = "A comprehensive overview of your literary engagement and year-to-date metrics.",
        metrics = listOf(
            StatsMetricDto("BOOKS READ", "42"),
            StatsMetricDto("TOTAL PAGES", "12,450"),
            StatsMetricDto("UNIQUE AUTHORS", "38"),
            StatsMetricDto("GENRES EXPLORED", "12")
        ),
        heatmapMonths = listOf("April", "May", "June"),
        heatmapRows = listOf("L", "M", "M", "J", "V", "S", "D"),
        heatmapValues = listOf(
            listOf(0.08f, 0.12f, 0.18f, 0.15f, 0.20f, 0.28f, 0.35f, 0.55f, 0.60f, 0.72f, 0.76f, 0.68f),
            listOf(0.05f, 0.10f, 0.16f, 0.12f, 0.22f, 0.36f, 0.45f, 0.58f, 0.62f, 0.75f, 0.82f, 0.74f),
            listOf(0.06f, 0.09f, 0.15f, 0.18f, 0.26f, 0.33f, 0.50f, 0.57f, 0.64f, 0.78f, 0.86f, 0.80f),
            listOf(0.04f, 0.08f, 0.14f, 0.20f, 0.29f, 0.41f, 0.47f, 0.61f, 0.67f, 0.70f, 0.78f, 0.73f),
            listOf(0.03f, 0.10f, 0.13f, 0.22f, 0.31f, 0.38f, 0.52f, 0.56f, 0.63f, 0.71f, 0.79f, 0.76f),
            listOf(0.02f, 0.07f, 0.12f, 0.18f, 0.24f, 0.30f, 0.43f, 0.50f, 0.58f, 0.66f, 0.74f, 0.69f),
            listOf(0.01f, 0.05f, 0.08f, 0.14f, 0.19f, 0.26f, 0.34f, 0.41f, 0.49f, 0.57f, 0.63f, 0.60f)
        ),
        radarSections = listOf(
            RadarSectionDto(
                mode = "Genre",
                axes = listOf(
                    RadarAxisDto("Adventure", 0.46f),
                    RadarAxisDto("Fantasy", 0.68f),
                    RadarAxisDto("Sci-Fi", 0.54f),
                    RadarAxisDto("Suspense", 0.42f),
                    RadarAxisDto("Horror", 0.34f),
                    RadarAxisDto("Romance", 0.52f),
                    RadarAxisDto("Drama", 0.74f),
                    RadarAxisDto("Mystery", 0.63f)
                ),
                ranking = listOf(
                    RankingItemDto(1, "Drama"),
                    RankingItemDto(2, "Science Fiction"),
                    RankingItemDto(3, "Fantasy"),
                    RankingItemDto(4, "Mystery"),
                    RankingItemDto(5, "Romance")
                )
            ),
            RadarSectionDto(
                mode = "Author",
                axes = listOf(
                    RadarAxisDto("Asimov", 0.72f),
                    RadarAxisDto("Le Guin", 0.58f),
                    RadarAxisDto("Murakami", 0.44f),
                    RadarAxisDto("King", 0.39f),
                    RadarAxisDto("Austen", 0.34f),
                    RadarAxisDto("Doyle", 0.49f),
                    RadarAxisDto("Tolkien", 0.81f),
                    RadarAxisDto("Atwood", 0.56f)
                ),
                ranking = listOf(
                    RankingItemDto(1, "J.R.R. Tolkien"),
                    RankingItemDto(2, "Isaac Asimov"),
                    RankingItemDto(3, "Ursula K. Le Guin"),
                    RankingItemDto(4, "Margaret Atwood"),
                    RankingItemDto(5, "Arthur Conan Doyle")
                )
            )
        )
    )

    private val notifications = NotificationsDto(
        title = "Activity and Notifications",
        items = listOf(
            NotificationEntryDto(
                id = "notif_1",
                type = NotificationTypes.LIKED_YOUR_REVIEW,
                timestamp = "HOY · 10:24",
                actor = NotificationActorDto("Juan", 0xFF35566F, 0xFFC8A988),
                fallbackText = "A Juan le gustó tu reseña."
            ),
            NotificationEntryDto(
                id = "notif_2",
                type = NotificationTypes.FOLLOWED_YOU,
                timestamp = "HOY · 07:12",
                actor = NotificationActorDto("Sofía", 0xFFB9CBE3, 0xFFE7EEF7),
                fallbackText = "Sofía comenzó a seguirte."
            ),
            NotificationEntryDto(
                id = "notif_3",
                type = NotificationTypes.REVIEWED_BOOK,
                timestamp = "AYER · 14:30",
                actor = NotificationActorDto("Elena", 0xFF534D61, 0xFFD9B89C),
                book = NotificationBookSummaryDto(
                    "El Laberinto de los Espíritus",
                    "CARLOS RUIZ ZAFÓN",
                    coverUrl("9788408163381")
                ),
                fallbackText = "Elena hizo una reseña sobre un libro."
            ),
            NotificationEntryDto(
                id = "notif_4",
                type = NotificationTypes.STARTED_READING,
                timestamp = "AYER · 09:18",
                actor = NotificationActorDto("Martina", 0xFF6D7FA2, 0xFFDAB596),
                book = NotificationBookSummaryDto(
                    "The Left Hand of Darkness",
                    "URSULA K. LE GUIN",
                    coverUrl("9780441478125")
                ),
                fallbackText = "Martina empezó a leer un nuevo libro."
            ),
            NotificationEntryDto(
                id = "notif_5",
                type = NotificationTypes.FOLLOWED_YOU,
                timestamp = "LUNES · 21:04",
                actor = NotificationActorDto("Tomás", 0xFF4E697F, 0xFFE6C7AA),
                fallbackText = "Tomás comenzó a seguirte."
            ),
            NotificationEntryDto(
                id = "notif_6",
                type = NotificationTypes.SAVED_YOUR_BOOK,
                timestamp = "LUNES · 17:42",
                actor = NotificationActorDto("Lucía", 0xFF7A8B6A, 0xFFDCC6A7),
                book = NotificationBookSummaryDto(
                    "Beloved",
                    "TONI MORRISON",
                    coverUrl("9781400033416")
                ),
                fallbackText = "Lucía guardó uno de tus libros en su lista."
            ),
            NotificationEntryDto(
                id = "notif_7",
                type = NotificationTypes.LIKED_YOUR_REVIEW,
                timestamp = "DOMINGO · 19:26",
                actor = NotificationActorDto("Bruno", 0xFF5A556A, 0xFFCDA58B),
                fallbackText = "A Bruno le gustó tu reseña."
            ),
            NotificationEntryDto(
                id = "notif_8",
                type = NotificationTypes.REVIEWED_BOOK,
                timestamp = "DOMINGO · 11:03",
                actor = NotificationActorDto("Camila", 0xFF7D6B8D, 0xFFE2C39F),
                book = NotificationBookSummaryDto(
                    "Piranesi",
                    "SUSANNA CLARKE",
                    coverUrl("9781635575637")
                ),
                fallbackText = "Camila hizo una reseña sobre un libro."
            ),
            NotificationEntryDto(
                id = "notif_9",
                type = NotificationTypes.FOLLOWED_YOU,
                timestamp = "SÁBADO · 16:58",
                actor = NotificationActorDto("Nicolás", 0xFF4D6B73, 0xFFD3B08C),
                fallbackText = "Nicolás comenzó a seguirte."
            ),
            NotificationEntryDto(
                id = "notif_10",
                type = "quote_liked",
                timestamp = "SÁBADO · 08:41",
                actor = NotificationActorDto("Irene", 0xFF607D8B, 0xFFE5CDB4),
                fallbackText = "A Irene le gustó tu cita destacada de \"The Waves\"."
            )
        )
    )
//    private val books = BookDto(
//
//    )
    fun observeOwnProfile(): StateFlow<ProfileDto> = ownProfileState

    fun observeOwnPublicPreview() = ownProfileState.map { profile ->
        profile.copy(
            profileStats = listOf(
                ProfileStatDto("Books read", profile.completedBooks.toString()),
                ProfileStatDto(
                    "Daily goal",
                    profile.readingGoal?.targetPagesPerDay?.let { "$it pgs" } ?: "None"
                )
            )
        )
    }

    fun observeOwnLibrary() = ownProfileState.map { profile ->
        LibraryDto(
            title = "My Library",
            subtitle = "Your personal collection, current read, and completed shelf.",
            avatar = profile.avatar,
            currentBook = profile.currentBook,
            readingBooks = listOf(
                LibraryBookDto(profile.currentBook.title, profile.currentBook.coverImageUrl),
                LibraryBookDto("Foucault's Pendulum", coverUrl("9780156032971")),
                LibraryBookDto("If on a winter's night a traveler", coverUrl("9780156439619"))
            ),
            shelfBooks = profile.publicLibrary,
            completedBooks = profile.completedBooks,
            readHistory = listOf(
                ReadBookDto("Beloved", "Toni Morrison", "Jan 12, 2026", "Jan 29, 2026", 5, 0xFF82645A),
                ReadBookDto("Pale Fire", "Vladimir Nabokov", "Feb 02, 2026", "Feb 18, 2026", 4, 0xFF627A92),
                ReadBookDto("The Waves", "Virginia Woolf", "Mar 03, 2026", "Mar 21, 2026", 5, 0xFF6C8A80),
                ReadBookDto("Never Let Me Go", "Kazuo Ishiguro", "Apr 01, 2026", "Apr 14, 2026", 4, 0xFF536E8A),
                ReadBookDto("Fictions", "Jorge Luis Borges", "Apr 20, 2026", "May 03, 2026", 5, 0xFF8C6B5A),
                ReadBookDto("The Secret History", "Donna Tartt", "May 08, 2026", "May 28, 2026", 5, 0xFF6E918B)
            )
        )
    }

    fun observeHomeFeed() = ownProfileState.map { profile ->
        HomeDto(
            trendingTitle = "Trending Now",
            avatar = profile.avatar,
            featuredBook = HomeFeaturedBookDto(
                label = "FEATURED",
                title = "The Midnight Library",
                author = "Matt Haig",
                coverImageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=1200&q=80"
            ),
            rankedBooks = listOf(
                HomeRankedBookDto(
                    rankLabel = "01",
                    title = "Circe",
                    author = "Madeline Miller",
                    coverImageUrl = coverUrl("9780316556323")
                ),
                HomeRankedBookDto(
                    rankLabel = "02",
                    title = "Piranesi",
                    author = "Susanna Clarke",
                    coverImageUrl = coverUrl("9781635575637")
                ),
                HomeRankedBookDto(
                    rankLabel = "03",
                    title = "Project Hail Mary",
                    author = "Andy Weir",
                    coverImageUrl = coverUrl("9780593135204")
                )
            ),
            readingRooms = listOf(
                HomeReadingRoomDto(
                    hostName = "Eleanor",
                    hostImageUrl = avatarUrl("eleanor"),
                    title = "Magical Realism Book Club",
                    readerCountLabel = "1.2k readers"
                ),
                HomeReadingRoomDto(
                    hostName = "James",
                    hostImageUrl = avatarUrl("james"),
                    title = "20th Century Classics",
                    readerCountLabel = "850 readers"
                )
            ),
            curators = listOf(
                HomeCuratorDto(
                    name = "Dr. Aris Thorne",
                    focus = "Historical Non-Fiction Focus",
                    imageUrl = avatarUrl("aris-thorne")
                ),
                HomeCuratorDto(
                    name = "Lila Vance",
                    focus = "Contemporary Lit & Essays",
                    imageUrl = avatarUrl("lila-vance")
                )
            )
        )
    }

    suspend fun getPublicProfile(): ProfileDto = publicProfile

    suspend fun updateOwnProfile(request: UpdateProfileRequestDto) {
        val previous = ownProfileState.value
        ownProfileState.value = previous.copy(
            name = request.name,
            handle = request.handle,
            quote = request.quote,
            avatar = previous.avatar.copy(
                topColorHex = request.avatarTopColorHex,
                bottomColorHex = request.avatarBottomColorHex,
                avatarPresetId = request.avatarPresetId,
                presetImageUrl = avatarPresets().firstOrNull { it.id == request.avatarPresetId }?.imageUrl,
                imageUri = request.avatarImageUri
            ),
            readingGoal = request.targetPagesPerDay?.let { target ->
                ReadingGoalDto(
                    targetPagesPerDay = target,
                    currentAveragePagesPerDay = previous.readingGoal?.currentAveragePagesPerDay ?: (target * 0.7f).toInt()
                )
            }
        )
    }

    suspend fun getStats(): StatsDto = stats

    suspend fun getNotifications(): NotificationsDto = notifications

    private val books = listOf(
        BookDto(
            id = "1",
            title = "The Secret History",
            author = "Donna Tartt",
            coverImageUrl = coverUrl("9781400031702"),
            genre = "Fiction",
            description = "A group of classics students at a small Vermont college become entangled in a murder.",
            pages = 559,
            language = "English",
            published = "27/05/1987",
            isbn = "987698762"
        ),
        BookDto(
            id = "2",
            title = "The Name of the Rose",
            author = "Umberto Eco",
            coverImageUrl = coverUrl("9780156001311"),
            genre = "Mystery",
            description = "A medieval monk investigates a series of mysterious deaths in an Italian abbey.",
            pages = 242,
            language = "English",
            published = "27/05/1927",
            isbn = "987618762"
        ),
        BookDto(
            id = "3",
            title = "Beloved",
            author = "Toni Morrison",
            coverImageUrl = coverUrl("9781400033416"),
            genre = "Fiction",
            description = "A former enslaved woman is haunted by the ghost of her daughter.",
            pages = 559,
            language = "English",
            published = "27/05/1986",
            isbn = "987618762"
        )
    )

    private val readingProgress = mapOf(
        "1" to ReadingProgressDto(bookId = "1", currentPage = 248, totalPages = 559, updatedAt = "11/05/2026"),
        "2" to ReadingProgressDto(bookId = "2", currentPage = 312, totalPages = 512, updatedAt = "11/05/2026")
    )

    private val reviews = mapOf(
        "1" to listOf(
            ReviewDto(
                id = "r1",
                reviewerName = "Evelyn Vance",
                reviewerAvatar = avatarUrl("witch"),
                rating = 5f,
                text = "A novel built on obsession, elitism and silence. Tartt makes every scene feel both intimate and dangerous.",
                likes = 42,
                createdAt = "2d ago"
            ),
            ReviewDto(
                id = "r2",
                reviewerName = "Julian Thorne",
                reviewerAvatar = avatarUrl("pirate"),
                rating = 4f,
                text = "Dense and rewarding. Every page pulls you deeper into its dark academia world.",
                likes = 18,
                createdAt = "1w ago"
            )
        ),
        "2" to listOf(
            ReviewDto(
                id = "r3",
                reviewerName = "Julian Thorne",
                reviewerAvatar = avatarUrl("pirate"),
                rating = 5f,
                text = "A profound meditation on destiny. The novel keeps its labyrinth open long after the final page.",
                likes = 31,
                createdAt = "4h ago"
            )
        ),
        "3" to listOf(
            ReviewDto(
                id = "r4",
                reviewerName = "Evelyn Vance",
                reviewerAvatar = avatarUrl("witch"),
                rating = 5f,
                text = "Morrison writes memory like weather. Every return to this novel feels heavier and more precise.",
                likes = 67,
                createdAt = "1w ago"
            )
        )
    )

    suspend fun getBooks(): List<BookDto> = books

    suspend fun getBookById(bookId: String): BookDto =
        books.find { it.id == bookId }
            ?: throw Exception("Book not found: $bookId")

    suspend fun getReadingProgress(bookId: String): ReadingProgressDto? =
        readingProgress[bookId]

    suspend fun getReviews(bookId: String): List<ReviewDto> =
        reviews[bookId] ?: emptyList()
    private val followedAuthors = mutableSetOf<String>()

    private val authors = listOf(
        AuthorDto(
            id = "a1",
            name = "Donna Tartt",
            birthYear = 1963,
            deathYear = null,
            nationality = "American",
            description = "Pulitzer Prize-winning author known for her intricate literary fiction.",
            biography = "Donna Tartt was born in Greenwood, Mississippi in 1963. She studied at the University of Mississippi and Bennington College, where she began writing her debut novel. Her first book, The Secret History, was published in 1992 to widespread acclaim. Known for her meticulous prose and infrequent output, she spent a decade on each of her novels. Her third novel, The Goldfinch, won the Pulitzer Prize for Fiction in 2014.",
            imageUrl = null,
            books = books.filter { it.id == "1" },
            followers = 14200
        ),
        AuthorDto(
            id = "a2",
            name = "Umberto Eco",
            birthYear = 1932,
            deathYear = 2016,
            nationality = "Italian",
            description = "Philosopher, semiotician and novelist renowned for his erudite fiction.",
            biography = "Umberto Eco was born in Alessandria, Italy in 1932. A professor of semiotics at the University of Bologna, he became one of Italy's most celebrated intellectuals. His debut novel, The Name of the Rose, published in 1980, became an international bestseller and established him as a major literary figure. His works blend medieval history, philosophy, and literary theory into dense, rewarding narratives.",
            imageUrl = null,
            books = books.filter { it.id == "2" },
            followers = 21500
        ),
        AuthorDto(
            id = "a3",
            name = "Toni Morrison",
            birthYear = 1931,
            deathYear = 2019,
            nationality = "American",
            description = "Nobel Prize-winning author whose work explores the African American experience.",
            biography = "Toni Morrison was born Chloe Ardelia Wofford in Lorain, Ohio in 1931. She studied at Howard University and Cornell, and worked as an editor at Random House before becoming a celebrated novelist. Her novel Beloved, published in 1987, won the Pulitzer Prize and later the Nobel Prize in Literature in 1993. Her prose, lyrical and unflinching, redefined American literature.",
            imageUrl = null,
            books = books.filter { it.id == "3" },
            followers = 38900
        )
    )

    suspend fun getAuthors(): List<AuthorDto> = authors

    suspend fun getAuthorById(authorId: String): AuthorDto =
        authors.find { it.id == authorId }
            ?: throw Exception("Author not found: $authorId")

    suspend fun toggleFollow(authorId: String) {
        if (authorId in followedAuthors) {
            followedAuthors.remove(authorId)
        } else {
            followedAuthors.add(authorId)
        }
    }
}
