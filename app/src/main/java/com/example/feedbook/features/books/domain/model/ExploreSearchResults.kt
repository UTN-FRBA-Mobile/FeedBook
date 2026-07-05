package com.example.feedbook.features.books.domain.model

import com.example.feedbook.features.authors.domain.model.Author

data class ExploreSearchResults(
    val books: List<Book> = emptyList(),
    val authors: List<Author> = emptyList(),
    val users: List<ExploreUser> = emptyList()
)
