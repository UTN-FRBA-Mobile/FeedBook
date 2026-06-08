package com.example.feedbook.features.books.domain.model

data class Book(
    val id: String,
    val authorId: String = "",
    val title: String,
    val author: String,
    val description: String,
    val coverImageUrl: String?,
    val language: String,
    val genre: String,
    val pages: Int,
    val published: String,
    val isbn: String,
)
