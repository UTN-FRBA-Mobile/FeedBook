package com.example.feedbook.features.books.presentation.all

import com.example.feedbook.features.books.presentation.detail.ReviewUiModel

data class AllReviewsUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val reviews: List<ReviewUiModel> = emptyList(),
    val total: Int = 0,
    val currentPage: Int = 1
)
