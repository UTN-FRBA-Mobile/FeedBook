package com.example.feedbook.features.profile.domain.model

data class ReaderProfile(
    val name: String,
    val handle: String,
    val quote: String,
    val avatar: AvatarInfo,
    val availableAvatarPresets: List<AvatarPresetInfo>,
    val readingGoal: ReadingGoal?,
    val readingStreak: ReadingStreak,
    val currentBook: CurrentBook,
    val upNextBooks: List<QueuedBook>,
    val completedBooks: Int,
    val profileStats: List<ProfileStat>,
    val publicLibrary: List<LibraryBook>,
    val featuredReviews: List<FeaturedReview>
)

data class AvatarInfo(
    val topColorHex: Long,
    val bottomColorHex: Long,
    val avatarPresetId: String?,
    val presetImageUrl: String?,
    val imageUri: String?
)

data class AvatarPresetInfo(
    val id: String,
    val topColorHex: Long,
    val bottomColorHex: Long,
    val imageUrl: String?
)

data class ReadingGoal(
    val targetPagesPerDay: Int,
    val currentAveragePagesPerDay: Int
)

data class ReadingStreak(
    val days: Int,
    val week: List<StreakDay>
)

data class StreakDay(
    val label: String,
    val fillFraction: Float,
    val isToday: Boolean,
    val completed: Boolean
)

data class CurrentBook(
    val id: String,
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

data class UpdateProfileCommand(
    val name: String,
    val handle: String,
    val quote: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val avatarPresetId: String?,
    val avatarImageUri: String?,
    val targetPagesPerDay: Int?
)
