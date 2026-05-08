package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.domain.model.ReaderProfile

fun ReaderProfile.toOwnProfileUiState(): ProfileUiState = toUiState(
    variant = ProfileVariant.OWN,
    actionLabel = "EDIT PROFILE",
    profileStats = profileStats.map { ProfileStat(it.label, it.value) }
)

fun ReaderProfile.toPublicProfileUiState(): ProfileUiState = toUiState(
    variant = ProfileVariant.PUBLIC,
    actionLabel = "FOLLOW",
    profileStats = profileStats.map { ProfileStat(it.label, it.value) }
)

private fun ReaderProfile.toUiState(
    variant: ProfileVariant,
    actionLabel: String,
    profileStats: List<ProfileStat>
): ProfileUiState = ProfileUiState(
    variant = variant,
    name = name,
    handle = handle,
    quote = quote,
    actionLabel = actionLabel,
    avatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    ),
    avatarImageUri = avatar.imageUri,
    readingGoal = readingGoal?.let {
        ReadingGoal(
            targetPagesPerDay = it.targetPagesPerDay,
            currentAveragePagesPerDay = it.currentAveragePagesPerDay
        )
    },
    readingStreak = ReadingStreak(
        days = readingStreak.days,
        week = readingStreak.week.map {
            StreakDay(
                label = it.label,
                fillFraction = it.fillFraction,
                isToday = it.isToday,
                completed = it.completed
            )
        }
    ),
    currentBook = CurrentBook(
        id = currentBook.id,
        title = currentBook.title,
        author = currentBook.author,
        page = currentBook.page,
        totalPages = currentBook.totalPages,
        progress = currentBook.progress,
        coverAccent = Color(currentBook.coverAccentHex)
    ),
    upNextBooks = upNextBooks.map { QueuedBook(it.title, it.author) },
    completedBooks = completedBooks,
    profileStats = profileStats,
    publicLibrary = publicLibrary.map { LibraryBook(it.title, Color(it.accentHex)) },
    featuredReviews = featuredReviews.map {
        FeaturedReview(
            bookTitle = it.bookTitle,
            rating = it.rating,
            timeAgo = it.timeAgo,
            excerpt = it.excerpt,
            accent = Color(it.accentHex)
        )
    }
)
