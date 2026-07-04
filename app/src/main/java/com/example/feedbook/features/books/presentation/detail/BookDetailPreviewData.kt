package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.books.domain.model.ReviewPart

object BookDetailPreviewData {

    val sampleBook = BookUiModel(
        id = "1",
        title = "El nombre del viento",
        author = "Patrick Rothfuss",
        description = "Una historia épica de magia y aventura...",
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
            reviewerName = "Juan Pérez",
            reviewerAvatar = null,
            rating = 4.5f,
            ratingText = "4.5 ★",
            text = "Excelente libro, muy recomendado. El giro final cambia todo.",
            parts = listOf(
                ReviewPart(text = "Excelente libro, muy recomendado. ", spoiler = false),
                ReviewPart(text = "El giro final cambia todo.", spoiler = true)
            ),
            likes = 128,
            likesText = "128 likes",
            isLikedByMe = false,
            createdAt = "2024-01-01"
        )
    )

    val sampleProgress = ReadingProgressUiModel(
        percentage = 45,
        progressText = "45% completado",
        currentPage = 90,
        totalPages = 200,
        pagesText = "Página 90 de 200"
    )

    val sampleState = BookDetailUiState(
        book = sampleBook,
        reviews = sampleReviews,
        readingProgress = sampleProgress
    )

    val loadingState = BookDetailUiState(isLoading = true)

    val errorState = BookDetailUiState(error = "No se pudo cargar el libro")
}
