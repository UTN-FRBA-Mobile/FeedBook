package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review

data class BookDetailState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val reviews: List<Review> = emptyList(),
    val readingProgress: ReadingProgress? = null,
    val error: String? = null
)