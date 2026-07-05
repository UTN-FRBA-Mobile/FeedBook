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
    val avatarPreset: AvatarPreset?,
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
    val errorMessage: String? = null,
    val isFollowing: Boolean = false
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
    val id: String,
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

data class AvatarPreset(
    val id: String,
    val labelRes: Int,
    val style: AvatarStyle,
    val imageUrl: String?
)

private val avatarPresetLabels = mapOf(
    "vampire" to R.string.avatar_preset_vampire,
    "werewolf" to R.string.avatar_preset_werewolf,
    "witch" to R.string.avatar_preset_witch,
    "wizard" to R.string.avatar_preset_wizard,
    "harry_potter" to R.string.avatar_preset_harry_potter,
    "astronaut" to R.string.avatar_preset_astronaut,
    "grim_reaper" to R.string.avatar_preset_grim_reaper,
    "fairy" to R.string.avatar_preset_fairy,
    "pirate" to R.string.avatar_preset_pirate,
    "princess" to R.string.avatar_preset_princess,
    "king" to R.string.avatar_preset_king,
    "ghost" to R.string.avatar_preset_ghost
)

fun avatarPresetFromData(
    id: String?,
    style: AvatarStyle,
    imageUrl: String?
): AvatarPreset? {
    val labelRes = id?.let(avatarPresetLabels::get) ?: return null
    return AvatarPreset(
        id = id,
        labelRes = labelRes,
        style = style,
        imageUrl = imageUrl
    )
}

fun defaultAvatarStyle(): AvatarStyle = AvatarStyle(
    topColor = Color(0xFFE4E9EE),
    bottomColor = Color(0xFFBCC7D1)
)

fun emptyProfileUiState(variant: ProfileVariant): ProfileUiState = ProfileUiState(
    variant = variant,
    name = "",
    handle = "",
    quote = "",
    actionLabelRes = if (variant == ProfileVariant.OWN) R.string.profile_action_edit else R.string.profile_action_follow,
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null,
    readingGoal = null,
    readingStreak = ReadingStreak(days = 0, week = emptyList()),
    currentBook = CurrentBook(
        id = "",
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
    featuredReviews = emptyList(),
    isFollowing = false
)
