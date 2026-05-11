package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetReviewsUseCase(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(bookId: String): List<Review> {
        return bookRepository.getReviews(bookId)
    }
}