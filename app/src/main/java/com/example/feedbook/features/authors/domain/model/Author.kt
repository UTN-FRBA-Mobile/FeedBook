package com.example.feedbook.features.authors.domain.model

import com.example.feedbook.features.books.domain.model.Book

data class Author(
    val id: String,
    val name: String,
    val birthYear: Int,
    val deathYear: Int?,
    val nationality: String,
    val description: String,
    val biography: String,
    val imageUrl: String?,
    val books: List<Book>,
    val followers: Int,
    val isFollowing: Boolean = false
)