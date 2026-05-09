package com.example.feedbook.shared.fakebackend

import com.example.feedbook.features.notifications.data.remote.dto.NotificationBookPreviewDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationEntryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.library.data.remote.dto.ReadBookDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
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
    private val ownProfileState = MutableStateFlow(
        ProfileDto(
            name = "Evelyn Vance",
            handle = "@evelynv",
            quote = "\"Reading is a conversation. All books talk. But a good book listens as well.\"",
            avatar = AvatarDto(0xFF315A73, 0xFFF0C6A8, null),
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
                title = "The Secret History",
                author = "Donna Tartt",
                page = 248,
                totalPages = 559,
                progress = 0.44f,
                coverAccentHex = 0xFF6E918B
            ),
            upNextBooks = listOf(
                QueuedBookDto("Foucault's Pendulum", "Umberto Eco"),
                QueuedBookDto("The Shadow of the Wind", "Carlos Ruiz Zafon"),
                QueuedBookDto("If on a winter's night a traveler", "Italo Calvino")
            ),
            completedBooks = 142,
            profileStats = listOf(
                ProfileStatDto("Books read", "142"),
                ProfileStatDto("This year", "19")
            ),
            publicLibrary = listOf(
                LibraryBookDto("The Secret History", 0xFF6E918B),
                LibraryBookDto("Fictions", 0xFF8C6B5A),
                LibraryBookDto("Never Let Me Go", 0xFF536E8A),
                LibraryBookDto("Beloved", 0xFF82645A),
                LibraryBookDto("Pale Fire", 0xFF627A92),
                LibraryBookDto("The Waves", 0xFF6C8A80)
            ),
            featuredReviews = listOf(
                FeaturedReviewDto(
                    bookTitle = "The Secret History",
                    rating = 5,
                    timeAgo = "2d ago",
                    excerpt = "\"A novel built on obsession, elitism and silence. Tartt makes every scene feel both intimate and dangerous.\"",
                    accentHex = 0xFF6E918B
                ),
                FeaturedReviewDto(
                    bookTitle = "Beloved",
                    rating = 5,
                    timeAgo = "1w ago",
                    excerpt = "\"Morrison writes memory like weather. Every return to this novel feels heavier and more precise.\"",
                    accentHex = 0xFF82645A
                )
            )
        )
    )

    private val publicProfile = ProfileDto(
        name = "Julian Thorne",
        handle = "@julianthorne",
        quote = "\"I collect stories that feel like half-remembered dreams and impossible cities.\"",
        avatar = AvatarDto(0xFF48627B, 0xFFE1B996, null),
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
            title = "The Name of the Rose",
            author = "Umberto Eco",
            page = 312,
            totalPages = 512,
            progress = 0.61f,
            coverAccentHex = 0xFF56728A
        ),
        upNextBooks = emptyList(),
        completedBooks = 58,
        profileStats = listOf(
            ProfileStatDto("Reviews", "128"),
            ProfileStatDto("Followers", "2.4K")
        ),
        publicLibrary = listOf(
            LibraryBookDto("One Hundred Years of Solitude", 0xFF9A7B5A),
            LibraryBookDto("The Shadow of the Wind", 0xFF5C6D8A),
                LibraryBookDto("Fictions", 0xFF6A8474),
            LibraryBookDto("Invisible Cities", 0xFF967E66),
            LibraryBookDto("Austerlitz", 0xFF7A8798),
            LibraryBookDto("If on a winter's night a traveler", 0xFF8A6B58),
            LibraryBookDto("The Left Hand of Darkness", 0xFF5D7287),
            LibraryBookDto("Pedro Paramo", 0xFF7B6A61),
            LibraryBookDto("The Master and Margarita", 0xFF5F7F74)
        ),
        featuredReviews = listOf(
            FeaturedReviewDto("The Name of the Rose", 5, "4h ago", "\"A profound meditation on destiny. The novel keeps its labyrinth open long after the final page.\"", 0xFF56728A),
            FeaturedReviewDto("Invisible Cities", 4, "3d ago", "\"Calvino turns urban imagination into something light and exact. Every fragment expands after you finish it.\"", 0xFF967E66),
            FeaturedReviewDto("Austerlitz", 5, "1w ago", "\"A quiet, relentless novel. Sebald makes memory feel architectural, fragile and impossible to escape.\"", 0xFF7A8798)
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
        heatmapRows = listOf("L", "M", "M", "J", "V", "S"),
        heatmapValues = listOf(
            listOf(0.08f, 0.12f, 0.18f, 0.15f, 0.20f, 0.28f, 0.35f, 0.55f, 0.60f, 0.72f, 0.76f, 0.68f),
            listOf(0.05f, 0.10f, 0.16f, 0.12f, 0.22f, 0.36f, 0.45f, 0.58f, 0.62f, 0.75f, 0.82f, 0.74f),
            listOf(0.06f, 0.09f, 0.15f, 0.18f, 0.26f, 0.33f, 0.50f, 0.57f, 0.64f, 0.78f, 0.86f, 0.80f),
            listOf(0.04f, 0.08f, 0.14f, 0.20f, 0.29f, 0.41f, 0.47f, 0.61f, 0.67f, 0.70f, 0.78f, 0.73f),
            listOf(0.03f, 0.10f, 0.13f, 0.22f, 0.31f, 0.38f, 0.52f, 0.56f, 0.63f, 0.71f, 0.79f, 0.76f),
            listOf(0.02f, 0.07f, 0.12f, 0.18f, 0.24f, 0.30f, 0.43f, 0.50f, 0.58f, 0.66f, 0.74f, 0.69f)
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
                    RankingItemDto(2, "Science Fiction")
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
                    RankingItemDto(2, "Isaac Asimov")
                )
            )
        )
    )

    private val notifications = NotificationsDto(
        title = "Activity and Notifications",
        items = listOf(
            NotificationEntryDto("Juan liked your review.\n\"A fascinating exploration of nourishment and memory. The prose moves effortlessly.\"", "TODAY · 10:24", 0xFF35566F, 0xFFC8A988, "♥", null),
            NotificationEntryDto("Sofia started following you.", "TODAY · 07:12", 0xFFB9CBE3, 0xFFE7EEF7, null, null),
            NotificationEntryDto("Elena shared a new book.", "YESTERDAY · 14:30", 0xFF534D61, 0xFFD9B89C, "⇪", NotificationBookPreviewDto("The Labyrinth of Spirits", "CARLOS RUIZ ZAFON", 0xFFD6E1EB)),
            NotificationEntryDto("Martina commented on your reading status.\n\"That ending stayed with me for days.\"", "YESTERDAY · 09:18", 0xFF6D7FA2, 0xFFDAB596, "✦", null),
            NotificationEntryDto("Tomas started following you.", "MONDAY · 21:04", 0xFF4E697F, 0xFFE6C7AA, null, null),
            NotificationEntryDto("Lucia saved one of your books to her reading list.", "MONDAY · 17:42", 0xFF7A8B6A, 0xFFDCC6A7, "⌁", null),
            NotificationEntryDto("Bruno liked your review of \"Beloved\".", "SUNDAY · 19:26", 0xFF5A556A, 0xFFCDA58B, "♥", null),
            NotificationEntryDto("Camila shared a new book.", "SUNDAY · 11:03", 0xFF7D6B8D, 0xFFE2C39F, "⇪", NotificationBookPreviewDto("Piranesi", "SUSANNA CLARKE", 0xFFC7D6DD)),
            NotificationEntryDto("Nicolas started following you.", "SATURDAY · 16:58", 0xFF4D6B73, 0xFFD3B08C, null, null),
            NotificationEntryDto("Irene liked your highlighted quote from \"The Waves\".", "SATURDAY · 08:41", 0xFF607D8B, 0xFFE5CDB4, "♥", null)
        )
    )

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
                LibraryBookDto(profile.currentBook.title, profile.currentBook.coverAccentHex),
                LibraryBookDto("Foucault's Pendulum", 0xFF7A8F89),
                LibraryBookDto("If on a winter's night a traveler", 0xFF8F745E)
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
}
