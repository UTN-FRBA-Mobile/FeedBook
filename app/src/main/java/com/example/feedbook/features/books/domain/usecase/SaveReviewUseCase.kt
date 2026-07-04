package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.repository.BookRepository
import com.example.feedbook.features.books.domain.model.ReviewPart

class SaveReviewUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String, rating: Float, text: String, parts: List<ReviewPart>) {
        bookRepository.saveReview(bookId, rating, text, parts)
    }
}
