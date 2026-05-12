package com.example.feedbook.features.books.presentation.list

import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser

data class BookListState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val authors: List<Author> = emptyList(),
    val users: List<ExploreUser> = emptyList(),
    val error: String? = null
)
