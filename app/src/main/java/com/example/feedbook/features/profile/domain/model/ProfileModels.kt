package com.example.feedbook.features.profile.domain.model

data class ReaderProfile(
    val name: String,
    val handle: String,
    val quote: String,
    val avatar: AvatarInfo,
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
    val imageUri: String?
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
    val coverAccentHex: Long
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
    val accentHex: Long
)

data class FeaturedReview(
    val bookTitle: String,
    val rating: Int,
    val timeAgo: String,
    val excerpt: String,
    val accentHex: Long
)

data class UpdateProfileCommand(
    val name: String,
    val handle: String,
    val quote: String,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val avatarImageUri: String?,
    val targetPagesPerDay: Int?
)
