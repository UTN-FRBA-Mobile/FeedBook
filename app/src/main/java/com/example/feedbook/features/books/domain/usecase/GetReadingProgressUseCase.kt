package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetReadingProgressUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): ReadingProgress? {
        return bookRepository.getReadingProgress(bookId)
    }
}