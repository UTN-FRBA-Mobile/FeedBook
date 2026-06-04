package com.example.feedbook.features.profile.presentation

import com.example.feedbook.R
import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.domain.model.ReaderProfile

fun ReaderProfile.toOwnProfileUiState(): ProfileUiState = toUiState(
    variant = ProfileVariant.OWN,
    actionLabelRes = R.string.profile_action_edit,
    profileStats = profileStats.map { ProfileStat(it.label, it.value) }
)

fun ReaderProfile.toPublicProfileUiState(): ProfileUiState = toUiState(
    variant = ProfileVariant.PUBLIC,
    actionLabelRes = R.string.profile_action_follow,
    profileStats = profileStats.map { ProfileStat(it.label, it.value) }
)

private fun ReaderProfile.toUiState(
    variant: ProfileVariant,
    actionLabelRes: Int,
    profileStats: List<ProfileStat>
): ProfileUiState {
    val avatarPresentation = toAvatarPresentation()
    return ProfileUiState(
        variant = variant,
        name = name,
        handle = handle,
        quote = quote,
        actionLabelRes = actionLabelRes,
        avatarStyle = avatarPresentation.style,
        avatarPreset = avatarPresentation.preset,
        avatarImageUri = avatarPresentation.imageUri,
        availableAvatarPresets = availableAvatarPresets.map {
            avatarPresetFromData(
                id = it.id,
                style = AvatarStyle(Color(it.topColorHex), Color(it.bottomColorHex)),
                imageUrl = it.imageUrl
            )
        }.filterNotNull(),
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
        coverImageUrl = currentBook.coverImageUrl
    ),
    upNextBooks = upNextBooks.map { QueuedBook(it.title, it.author, it.coverImageUrl) },
    completedBooks = completedBooks,
    profileStats = profileStats,
    publicLibrary = publicLibrary.map { LibraryBook(it.id, it.title, it.coverImageUrl) },
    featuredReviews = featuredReviews.map {
        FeaturedReview(
            bookTitle = it.bookTitle,
            rating = it.rating,
            timeAgo = it.timeAgo,
            excerpt = it.excerpt,
            coverImageUrl = it.coverImageUrl
        )
    }
)
}
