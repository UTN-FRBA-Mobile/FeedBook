package com.example.feedbook.features.books.presentation.detail

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review

fun Book.toUiModel() = BookUiModel(
    id = id,
    authorId = authorId,
    title = title,
    author = author,
    description = description,
    coverImageUrl = coverImageUrl,
    published = published,
    isbn = isbn,
    genre = genre,
    pages = pages,
    language = language
)

fun Review.toUiModel() = ReviewUiModel(
    id = id,
    userId = userId,
    reviewerName = reviewerName,
    reviewerAvatar = reviewerAvatar,
    rating = rating,
    ratingText = "%.1f ★".format(rating),
    text = text,
    parts = parts,
    likes = likes,
    likesText = if (likes == 1) "1 like" else "$likes likes",
    isLikedByMe = isLikedByMe,
    createdAt = createdAt
)

fun ReadingProgress.toUiModel() = ReadingProgressUiModel(
    percentage = if (totalPages > 0) {
        ((currentPage.toFloat() / totalPages) * 100).toInt()
    } else {
        0
    },
    progressText = if (totalPages > 0) {
        "${((currentPage.toFloat() / totalPages) * 100).toInt()}% completed"
    } else {
        "0% completed"
    },
    currentPage = currentPage,
    totalPages = totalPages,
    pagesText = "Page $currentPage of $totalPages"
)
