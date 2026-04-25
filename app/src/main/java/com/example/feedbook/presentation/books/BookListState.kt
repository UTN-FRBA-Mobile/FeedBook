package com.example.feedbook.presentation.books

import com.example.feedbook.domain.model.Book

data class BookListState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)
