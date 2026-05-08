package com.example.feedbook.features.books.domain.model

data class ReadingProgress (
    val bookId: String,
    val currentPage: Number,
    val totalPages: Number,
    val updatedAt: String
)