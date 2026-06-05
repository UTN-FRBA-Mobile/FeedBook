package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.domain.model.Review

fun ReviewDto.toDomain(): Review = Review(
    id = id,
    userId = userId,
    reviewerName = reviewerName,
    reviewerAvatar = reviewerAvatar,
    rating = rating,
    text = text,
    likes = likes,
    likedBy = likedBy,
    isLikedByMe = likedBy.contains("me"),
    createdAt = createdAt
)