package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color

data class ProfileUiState(
    val variant: ProfileVariant,
    val name: String,
    val handle: String,
    val quote: String,
    val actionLabel: String,
    val avatarStyle: AvatarStyle,
    val avatarImageUri: String?,
    val readingGoal: ReadingGoal?,
    val readingStreak: ReadingStreak,
    val currentBook: CurrentBook,
    val upNextBooks: List<QueuedBook>,
    val completedBooks: Int,
    val profileStats: List<ProfileStat>,
    val publicLibrary: List<LibraryBook>,
    val featuredReviews: List<FeaturedReview>
)

enum class ProfileVariant {
    OWN,
    PUBLIC
}

data class ReadingStreak(
    val days: Int,
    val week: List<StreakDay>
)

data class StreakDay(
    val label: String,
    val fillFraction: Float,
    val isToday: Boolean = false,
    val completed: Boolean = false
)

data class CurrentBook(
    val id: String,
    val title: String,
    val author: String,
    val page: Int,
    val totalPages: Int,
    val progress: Float,
    val coverAccent: Color
)

data class QueuedBook(
    val title: String,
    val author: String
)

data class ProfileStat(
    val label: String,
    val value: String
)

data class LibraryBook(
    val title: String,
    val accent: Color
)

data class FeaturedReview(
    val bookTitle: String,
    val rating: Int,
    val timeAgo: String,
    val excerpt: String,
    val accent: Color
)

data class ReadingGoal(
    val targetPagesPerDay: Int,
    val currentAveragePagesPerDay: Int
)

data class AvatarStyle(
    val topColor: Color,
    val bottomColor: Color
)

fun sampleProfileUiState(): ProfileUiState = ProfileUiState(
    variant = ProfileVariant.OWN,
    name = "Evelyn Vance",
    handle = "@evelynv",
    quote = "\"Reading is a conversation. All books talk. But a good book listens as well.\"",
    actionLabel = "EDIT PROFILE",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarImageUri = null,
    readingGoal = ReadingGoal(
        targetPagesPerDay = 40,
        currentAveragePagesPerDay = 28
    ),
    readingStreak = ReadingStreak(
        days = 5,
        week = listOf(
            StreakDay(label = "M", fillFraction = 0.18f),
            StreakDay(label = "T", fillFraction = 0.72f, completed = true),
            StreakDay(label = "W", fillFraction = 1f, completed = true),
            StreakDay(label = "T", fillFraction = 0.48f, completed = true),
            StreakDay(label = "F", fillFraction = 1f, completed = true),
            StreakDay(label = "S", fillFraction = 0.88f, completed = true),
            StreakDay(label = "S", fillFraction = 0f, isToday = true)
        )
    ),
    currentBook = CurrentBook(
        id = "1",
        title = "The Secret History",
        author = "Donna Tartt",
        page = 248,
        totalPages = 559,
        progress = 0.44f,
        coverAccent = Color(0xFF6E918B)
    ),
    upNextBooks = listOf(
        QueuedBook("Foucault's Pendulum", "Umberto Eco"),
        QueuedBook("The Shadow of the Wind", "Carlos Ruiz Zafon"),
        QueuedBook("If on a winter's night a traveler", "Italo Calvino")
    ),
    completedBooks = 142,
    profileStats = listOf(
        ProfileStat(label = "Books read", value = "142"),
        ProfileStat(label = "This year", value = "19")
    ),
    publicLibrary = listOf(
        LibraryBook("The Secret History", Color(0xFF6E918B)),
        LibraryBook("Ficciones", Color(0xFF8C6B5A)),
        LibraryBook("Never Let Me Go", Color(0xFF536E8A)),
        LibraryBook("Beloved", Color(0xFF82645A)),
        LibraryBook("Pale Fire", Color(0xFF627A92)),
        LibraryBook("The Waves", Color(0xFF6C8A80))
    ),
    featuredReviews = listOf(
        FeaturedReview(
            bookTitle = "The Secret History",
            rating = 5,
            timeAgo = "2d ago",
            excerpt = "\"A novel built on obsession, elitism and silence. Tartt makes every scene feel both intimate and dangerous.\"",
            accent = Color(0xFF6E918B)
        ),
        FeaturedReview(
            bookTitle = "Beloved",
            rating = 5,
            timeAgo = "1w ago",
            excerpt = "\"Morrison writes memory like weather. Every return to this novel feels heavier and more precise.\"",
            accent = Color(0xFF82645A)
        )
    )
)

fun samplePublicProfileUiState(): ProfileUiState = ProfileUiState(
    variant = ProfileVariant.PUBLIC,
    name = "Julian Thorne",
    handle = "@julianthorne",
    quote = "\"I collect stories that feel like half-remembered dreams and impossible cities.\"",
    actionLabel = "FOLLOW",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF48627B),
        bottomColor = Color(0xFFE1B996)
    ),
    avatarImageUri = null,
    readingGoal = null,
    readingStreak = ReadingStreak(
        days = 0,
        week = listOf(
            StreakDay(label = "M", fillFraction = 0f),
            StreakDay(label = "T", fillFraction = 0f),
            StreakDay(label = "W", fillFraction = 0f),
            StreakDay(label = "T", fillFraction = 0f),
            StreakDay(label = "F", fillFraction = 0f),
            StreakDay(label = "S", fillFraction = 0f),
            StreakDay(label = "S", fillFraction = 0f, isToday = true)
        )
    ),
    currentBook = CurrentBook(
        id = "2",
        title = "The Name of the Rose",
        author = "Umberto Eco",
        page = 312,
        totalPages = 512,
        progress = 0.61f,
        coverAccent = Color(0xFF56728A)
    ),
    upNextBooks = emptyList(),
    completedBooks = 58,
    profileStats = listOf(
        ProfileStat(label = "Reviews", value = "128"),
        ProfileStat(label = "Followers", value = "2.4K")
    ),
    publicLibrary = listOf(
        LibraryBook("One Hundred Years of Solitude", Color(0xFF9A7B5A)),
        LibraryBook("The Shadow of the Wind", Color(0xFF5C6D8A)),
        LibraryBook("Ficciones", Color(0xFF6A8474)),
        LibraryBook("Invisible Cities", Color(0xFF967E66)),
        LibraryBook("Austerlitz", Color(0xFF7A8798)),
        LibraryBook("If on a winter's night a traveler", Color(0xFF8A6B58)),
        LibraryBook("The Left Hand of Darkness", Color(0xFF5D7287)),
        LibraryBook("Pedro Paramo", Color(0xFF7B6A61)),
        LibraryBook("The Master and Margarita", Color(0xFF5F7F74))
    ),
    featuredReviews = listOf(
        FeaturedReview(
            bookTitle = "The Name of the Rose",
            rating = 5,
            timeAgo = "4h ago",
            excerpt = "\"A profound meditation on destiny. The novel keeps its labyrinth open long after the final page.\"",
            accent = Color(0xFF56728A)
        ),
        FeaturedReview(
            bookTitle = "Invisible Cities",
            rating = 4,
            timeAgo = "3d ago",
            excerpt = "\"Calvino turns urban imagination into something light and exact. Every fragment expands after you finish it.\"",
            accent = Color(0xFF967E66)
        ),
        FeaturedReview(
            bookTitle = "Austerlitz",
            rating = 5,
            timeAgo = "1w ago",
            excerpt = "\"A quiet, relentless novel. Sebald makes memory feel architectural, fragile and impossible to escape.\"",
            accent = Color(0xFF7A8798)
        )
    )
)
