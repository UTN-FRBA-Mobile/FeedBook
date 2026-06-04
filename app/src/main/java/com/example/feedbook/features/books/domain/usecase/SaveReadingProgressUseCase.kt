package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.repository.BookRepository

class SaveReadingProgressUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String, currentPage: Int): ReadingProgress {
        return bookRepository.saveReadingProgress(bookId, currentPage)
    }
}
