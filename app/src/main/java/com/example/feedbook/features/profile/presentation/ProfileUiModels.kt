package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.R

data class ProfileUiState(
    val variant: ProfileVariant,
    val name: String,
    val handle: String,
    val quote: String,
    val actionLabelRes: Int,
    val avatarStyle: AvatarStyle,
    val avatarImageUri: String?,
    val readingGoal: ReadingGoal?,
    val readingStreak: ReadingStreak,
    val currentBook: CurrentBook,
    val upNextBooks: List<QueuedBook>,
    val completedBooks: Int,
    val profileStats: List<ProfileStat>,
    val publicLibrary: List<LibraryBook>,
    val featuredReviews: List<FeaturedReview>,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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
    val title: String,
    val author: String,
    val page: Int,
    val totalPages: Int,
    val progress: Float,
    val coverImageUrl: String?
)

data class QueuedBook(
    val title: String,
    val author: String,
    val coverImageUrl: String?
)

data class ProfileStat(
    val label: String,
    val value: String
)

data class LibraryBook(
    val title: String,
    val coverImageUrl: String?
)

data class FeaturedReview(
    val bookTitle: String,
    val rating: Int,
    val timeAgo: String,
    val excerpt: String,
    val coverImageUrl: String?
)

data class ReadingGoal(
    val targetPagesPerDay: Int,
    val currentAveragePagesPerDay: Int
)

data class AvatarStyle(
    val topColor: Color,
    val bottomColor: Color
)

fun defaultAvatarStyle(): AvatarStyle = AvatarStyle(
    topColor = Color(0xFF315A73),
    bottomColor = Color(0xFFF0C6A8)
)

fun emptyProfileUiState(variant: ProfileVariant): ProfileUiState = ProfileUiState(
    variant = variant,
    name = "",
    handle = "",
    quote = "",
    actionLabelRes = if (variant == ProfileVariant.OWN) R.string.profile_action_edit else R.string.profile_action_follow,
    avatarStyle = defaultAvatarStyle(),
    avatarImageUri = null,
    readingGoal = null,
    readingStreak = ReadingStreak(days = 0, week = emptyList()),
    currentBook = CurrentBook(
        title = "",
        author = "",
        page = 0,
        totalPages = 0,
        progress = 0f,
        coverImageUrl = null
    ),
    upNextBooks = emptyList(),
    completedBooks = 0,
    profileStats = emptyList(),
    publicLibrary = emptyList(),
    featuredReviews = emptyList()
)
