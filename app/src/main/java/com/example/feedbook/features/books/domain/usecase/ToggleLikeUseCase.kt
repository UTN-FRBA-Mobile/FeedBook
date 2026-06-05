package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.repository.BookRepository

class ToggleLikeUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String, reviewId: String): Review {
        return bookRepository.toggleLike(bookId, reviewId)
    }
}
