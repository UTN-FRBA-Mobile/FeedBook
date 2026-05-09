package com.example.feedbook.features.books.domain.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val coverImageUrl: String?
)
