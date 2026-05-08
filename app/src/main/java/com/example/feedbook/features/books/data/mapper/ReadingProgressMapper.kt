package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.domain.model.ReadingProgress

fun ReadingProgressDto.toDomain(): ReadingProgress = ReadingProgress(
    bookId = bookId,
    currentPage = currentPage,
    totalPages = totalPages,
    updatedAt = updatedAt
)