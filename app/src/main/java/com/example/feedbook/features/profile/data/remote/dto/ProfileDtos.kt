package com.example.feedbook.features.profile.data.remote.dto

data class ProfileDto(
    val name: String,
    val handle: String,
    val quote: String,
    val avatar: AvatarDto,
    val readingGoal: ReadingGoalDto?,
    val readingStreak: ReadingStreakDto,
    val currentBook: CurrentBookDto,
    val upNextBooks: List<QueuedBookDto>,
    val completedBooks: Int,
    val profileStats: List<ProfileStatDto>,
    val publicLibrary: List<LibraryBookDto>,
    val featuredReviews: List<FeaturedReviewDto>
)

data class AvatarDto(
    val topColorHex: Long,
    val bottomColorHex: Long,
    val imageUri: String?
)

data class ReadingGoalDto(
    val targetPagesPerDay: Int,
    val currentAveragePagesPerDay: Int
)

data class ReadingStreakDto(
    val days: Int,
    val week: List<StreakDayDto>
)

data class StreakDayDto(
    val label: String,
    val fillFraction: Float,
    val isToday: Boolean,
    val completed: Boolean
)

data class CurrentBookDto(
    val title: String,
    val author: String,
    val page: Int,
    val totalPages: Int,
    val progress: Float,
    val coverAccentHex: Long
)

data class QueuedBookDto(
    val title: String,
    val author: String
)

data class ProfileStatDto(
    val label: String,
    val value: String
)

data class LibraryBookDto(
    val title: String,
    val accentHex: Long
)

data class FeaturedReviewDto(
    val bookTitle: String,
    val rating: Int,
    val timeAgo: String,
    val excerpt: String,
    val accentHex: Long
)

data class UpdateProfileRequestDto(
    val name: String,
    val handle: String,
    val quote: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val avatarImageUri: String?,
    val targetPagesPerDay: Int?
)
