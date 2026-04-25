package com.example.feedbook.presentation.detail

import com.example.feedbook.domain.model.Book

data class BookDetailState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null
)
