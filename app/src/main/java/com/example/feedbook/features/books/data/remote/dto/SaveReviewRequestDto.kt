package com.example.feedbook.features.books.data.remote.dto

data class SaveReviewRequestDto(
    val rating: Float,
    val text: String,
    val parts: List<ReviewPartDto> = emptyList()
)
