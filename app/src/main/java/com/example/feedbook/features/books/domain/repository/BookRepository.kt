package com.example.feedbook.features.books.domain.repository

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.model.ReviewPart

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getExploreUsers(): List<ExploreUser>
    suspend fun getBookById(bookId: String): Book
    suspend fun getBookByIsbn(isbn: String): Book
    suspend fun getReviews(bookId: String, page: Int = 1, limit: Int = 5): Pair<List<Review>, Int>
    suspend fun saveReview(bookId: String, rating: Float, text: String, parts: List<ReviewPart> = emptyList()): Review
    suspend fun getReadingProgress(bookId: String): ReadingProgress?
    suspend fun saveReadingProgress(bookId: String, currentPage: Int): ReadingProgress
    suspend fun toggleLike(bookId: String, reviewId: String): Review
}
