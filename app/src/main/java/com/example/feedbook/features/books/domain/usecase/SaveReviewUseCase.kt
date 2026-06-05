package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.repository.BookRepository

class SaveReviewUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String, rating: Float, text: String) {
        bookRepository.saveReview(bookId, rating, text)
    }
}
