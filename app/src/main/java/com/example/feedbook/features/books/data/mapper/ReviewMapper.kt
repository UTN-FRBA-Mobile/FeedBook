package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.data.remote.dto.ReviewPartDto
import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.model.ReviewPart

fun ReviewDto.toDomain(): Review = Review(
    id = id,
    userId = userId,
    reviewerName = reviewerName,
    reviewerAvatar = reviewerAvatar,
    rating = rating,
    text = text,
    parts = parts.map { it.toDomain() },
    likes = likes,
    likedBy = likedBy,
    isLikedByMe = likedBy.contains("me"),
    createdAt = createdAt
)

fun ReviewPartDto.toDomain(): ReviewPart = ReviewPart(
    text = text,
    spoiler = spoiler
)

fun ReviewPart.toDto(): ReviewPartDto = ReviewPartDto(
    text = text,
    spoiler = spoiler
)
