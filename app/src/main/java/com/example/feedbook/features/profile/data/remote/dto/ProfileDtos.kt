package com.example.feedbook.features.profile.data.remote.dto

data class ProfileDto(
    val name: String,
    val handle: String,
    val quote: String,
    val avatar: AvatarDto,
    val availableAvatarPresets: List<AvatarPresetDto>?,
    val readingGoal: ReadingGoalDto?,
    val readingStreak: ReadingStreakDto,
    val currentBook: CurrentBookDto,
    val upNextBooks: List<QueuedBookDto>?,
    val completedBooks: Int,
    val profileStats: List<ProfileStatDto>?,
    val publicLibrary: List<LibraryBookDto>?,
    val featuredReviews: List<FeaturedReviewDto>?
)

data class AvatarDto(
    val topColorHex: Long,
    val bottomColorHex: Long,
    val avatarPresetId: String?,
    val presetImageUrl: String?,
    val imageUri: String?
)

data class AvatarPresetDto(
    val id: String,
    val topColorHex: Long,
    val bottomColorHex: Long,
    val imageUrl: String?
)

data class ReadingGoalDto(
    val targetPagesPerDay: Int,
    val currentAveragePagesPerDay: Int
)

data class ReadingStreakDto(
    val days: Int,
    val week: List<StreakDayDto>?
)

data class StreakDayDto(
    val label: String,
    val fillFraction: Float,
    val isToday: Boolean,
    val completed: Boolean
)

data class CurrentBookDto(
    val id: String,
    val title: String,
    val author: String,
    val page: Int,
    val totalPages: Int,
    val progress: Float,
    val coverImageUrl: String?
)

data class QueuedBookDto(
    val title: String,
    val author: String,
    val coverImageUrl: String? = null
)

data class ProfileStatDto(
    val label: String,
    val value: String
)

data class LibraryBookDto(
    val title: String,
    val coverImageUrl: String?
)

data class FeaturedReviewDto(
    val bookTitle: String,
    val rating: Int,
    val timeAgo: String,
    val excerpt: String,
    val coverImageUrl: String?
)

data class UpdateProfileRequestDto(
    val name: String,
    val handle: String,
    val quote: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val avatarPresetId: String?,
    val avatarImageUri: String?,
    val targetPagesPerDay: Int?
)
