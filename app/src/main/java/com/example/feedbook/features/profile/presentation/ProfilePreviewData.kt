package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color

private fun previewCoverUrl(isbn: String): String =
    "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg"

fun previewOwnProfileUiState(): ProfileUiState = emptyProfileUiState(ProfileVariant.OWN).copy(
    name = "Evelyn Vance",
    handle = "@evelynv",
    quote = "\"Reading is a conversation. All books talk. But a good book listens as well.\"",
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
        title = "The Secret History",
        author = "Donna Tartt",
        page = 248,
        totalPages = 559,
        progress = 0.44f,
        coverImageUrl = previewCoverUrl("9781400031702")
    ),
    upNextBooks = listOf(
        QueuedBook("Foucault's Pendulum", "Umberto Eco", previewCoverUrl("9780156032971")),
        QueuedBook("The Shadow of the Wind", "Carlos Ruiz Zafon", previewCoverUrl("9780143034902")),
        QueuedBook("If on a winter's night a traveler", "Italo Calvino", previewCoverUrl("9780156439619"))
    ),
    completedBooks = 142,
    profileStats = listOf(
        ProfileStat(label = "Books read", value = "142"),
        ProfileStat(label = "This year", value = "19")
    ),
    publicLibrary = listOf(
        LibraryBook("The Secret History", previewCoverUrl("9781400031702")),
        LibraryBook("Ficciones", previewCoverUrl("9780802130303")),
        LibraryBook("Never Let Me Go", previewCoverUrl("9781400078776")),
        LibraryBook("Beloved", previewCoverUrl("9781400033416")),
        LibraryBook("Pale Fire", previewCoverUrl("9780679723424")),
        LibraryBook("The Waves", previewCoverUrl("9780156949606"))
    ),
    featuredReviews = listOf(
        FeaturedReview(
            bookTitle = "The Secret History",
            rating = 5,
            timeAgo = "2d ago",
            excerpt = "\"A novel built on obsession, elitism and silence. Tartt makes every scene feel both intimate and dangerous.\"",
            coverImageUrl = previewCoverUrl("9781400031702")
        ),
        FeaturedReview(
            bookTitle = "Beloved",
            rating = 5,
            timeAgo = "1w ago",
            excerpt = "\"Morrison writes memory like weather. Every return to this novel feels heavier and more precise.\"",
            coverImageUrl = previewCoverUrl("9781400033416")
        )
    )
)

fun previewPublicProfileUiState(): ProfileUiState = emptyProfileUiState(ProfileVariant.PUBLIC).copy(
    name = "Julian Thorne",
    handle = "@julianthorne",
    quote = "\"I collect stories that feel like half-remembered dreams and impossible cities.\"",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF48627B),
        bottomColor = Color(0xFFE1B996)
    ),
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
        title = "The Name of the Rose",
        author = "Umberto Eco",
        page = 312,
        totalPages = 512,
        progress = 0.61f,
        coverImageUrl = previewCoverUrl("9780156001311")
    ),
    completedBooks = 58,
    profileStats = listOf(
        ProfileStat(label = "Reviews", value = "128"),
        ProfileStat(label = "Followers", value = "2.4K")
    ),
    publicLibrary = listOf(
        LibraryBook("One Hundred Years of Solitude", previewCoverUrl("9780060883287")),
        LibraryBook("The Shadow of the Wind", previewCoverUrl("9780143034902")),
        LibraryBook("Ficciones", previewCoverUrl("9780802130303")),
        LibraryBook("Invisible Cities", previewCoverUrl("9780156453806")),
        LibraryBook("Austerlitz", previewCoverUrl("9780811216548")),
        LibraryBook("If on a winter's night a traveler", previewCoverUrl("9780156439619")),
        LibraryBook("The Left Hand of Darkness", previewCoverUrl("9780441478125")),
        LibraryBook("Pedro Paramo", previewCoverUrl("9780802133908")),
        LibraryBook("The Master and Margarita", previewCoverUrl("9780143108276"))
    ),
    featuredReviews = listOf(
        FeaturedReview(
            bookTitle = "The Name of the Rose",
            rating = 5,
            timeAgo = "4h ago",
            excerpt = "\"A profound meditation on destiny. The novel keeps its labyrinth open long after the final page.\"",
            coverImageUrl = previewCoverUrl("9780156001311")
        ),
        FeaturedReview(
            bookTitle = "Invisible Cities",
            rating = 4,
            timeAgo = "3d ago",
            excerpt = "\"Calvino turns urban imagination into something light and exact. Every fragment expands after you finish it.\"",
            coverImageUrl = previewCoverUrl("9780156453806")
        ),
        FeaturedReview(
            bookTitle = "Austerlitz",
            rating = 5,
            timeAgo = "1w ago",
            excerpt = "\"A quiet, relentless novel. Sebald makes memory feel architectural, fragile and impossible to escape.\"",
            coverImageUrl = previewCoverUrl("9780811216548")
        )
    )
)
