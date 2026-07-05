package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.books.domain.model.ExploreUser

fun ExploreUser.toProfileUiState(): ProfileUiState {
    val avatarStyle = AvatarStyle(
        topColor = Color(avatarTopColorHex),
        bottomColor = Color(avatarBottomColorHex)
    )
    return emptyProfileUiState(ProfileVariant.PUBLIC).copy(
        name = name,
        handle = handle,
        quote = bio,
        actionLabelRes = if (isFollowing) {
            com.example.feedbook.R.string.profile_action_following
        } else {
            com.example.feedbook.R.string.profile_action_follow
        },
        avatarStyle = avatarStyle,
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
        profileStats = listOf(
            ProfileStat(label = "Followers", value = followersLabel),
            ProfileStat(label = "Books read", value = booksReadLabel)
        ),
        publicLibrary = emptyList(),
        featuredReviews = emptyList()
    ).copy(isFollowing = isFollowing)
}
