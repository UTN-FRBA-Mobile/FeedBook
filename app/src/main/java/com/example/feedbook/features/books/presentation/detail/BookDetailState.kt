package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.books.domain.model.Book

data class BookDetailState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null
)
