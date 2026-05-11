package com.example.feedbook.features.books.domain.model

data class ReadingProgress (
    val bookId: String,
    val currentPage: Int,
    val totalPages: Int,
    val updatedAt: String
)