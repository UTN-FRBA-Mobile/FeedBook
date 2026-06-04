package com.example.feedbook.features.books.domain.model

data class Review(
    val id: String,
    val userId: String,
    val reviewerName: String,
    val reviewerAvatar: String?,
    val rating: Float,
    val text: String,
    val likes: Int,
    val createdAt: String
)