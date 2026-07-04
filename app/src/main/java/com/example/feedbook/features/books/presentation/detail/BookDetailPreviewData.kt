package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.books.domain.model.ReviewPart

object BookDetailPreviewData {

    val sampleBook = BookUiModel(
        id = "1",
        title = "The Name of the Wind",
        author = "Patrick Rothfuss",
        description = "An epic tale of magic and adventure...",
        coverImageUrl = null,
        published = "March 27, 2007",
        pages = 662,
        language = "English (US)",
        genre = "Classic Fiction",
        isbn = "9788445015407"
    )

    val sampleReviews = listOf(
        ReviewUiModel(
            id = "1",
            userId = "",
            reviewerName = "Juan Perez",
            reviewerAvatar = null,
            rating = 4.5f,
            ratingText = "4.5 ★",
            text = "Excellent book, highly recommended. The final twist changes everything.",
            parts = listOf(
                ReviewPart(text = "Excellent book, highly recommended. ", spoiler = false),
                ReviewPart(text = "The final twist changes everything.", spoiler = true)
            ),
            likes = 128,
            likesText = "128 likes",
            isLikedByMe = false,
            createdAt = "2024-01-01"
        )
    )

    val sampleProgress = ReadingProgressUiModel(
        percentage = 45,
        progressText = "45% completed",
        currentPage = 90,
        totalPages = 200,
        pagesText = "Page 90 of 200"
    )

    val sampleState = BookDetailUiState(
        book = sampleBook,
        reviews = sampleReviews,
        readingProgress = sampleProgress
    )

    val loadingState = BookDetailUiState(isLoading = true)

    val errorState = BookDetailUiState(error = "Unable to load the book")
}
