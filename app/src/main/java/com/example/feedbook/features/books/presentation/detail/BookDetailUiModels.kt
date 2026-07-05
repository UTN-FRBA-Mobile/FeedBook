package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.model.ReviewPart

data class BookDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val book: BookUiModel? = null,
    val reviews: List<ReviewUiModel> = emptyList(),
    val userReview: ReviewUiModel? = null,
    val readingProgress: ReadingProgressUiModel? = null,
    val allReviewsTotal: Int = 0,
    val avatarStyle: AvatarStyle = defaultAvatarStyle(),
    val avatarPreset: AvatarPreset? = null,
    val avatarImageUri: String? = null,
    val isBookInLibrary: Boolean = false,
    val isTogglingLibrary: Boolean = false,
    val libraryFeedback: String? = null,
    val reviewFeedback: String? = null,
    val isSavingReview: Boolean = false,
    val bookUsers: List<ExploreUser> = emptyList()
)

data class BookUiModel(
    val id: String,
    val authorId: String = "",
    val pages: Int,
    val language: String,
    val published: String,
    val genre: String,
    val isbn: String,
    val title: String,
    val author: String,
    val description: String,
    val coverImageUrl: String?
)

data class ReviewUiModel(
    val id: String,
    val userId: String,
    val reviewerName: String,
    val reviewerAvatar: String?,
    val rating: Float,
    val ratingText: String,
    val text: String,
    val parts: List<ReviewPart> = emptyList(),
    val likes: Int,
    val likesText: String,
    val isLikedByMe: Boolean,
    val createdAt: String
)

data class ReadingProgressUiModel(
    val percentage: Int,
    val progressText: String,
    val currentPage: Int,
    val totalPages: Int,
    val pagesText: String
)
