package com.example.feedbook.features.books.domain.repository

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getExploreUsers(): List<ExploreUser>
    suspend fun getBookById(bookId: String): Book
    suspend fun getReviews(bookId: String): List<Review>
    suspend fun saveReview(bookId: String, rating: Float, text: String): Review
    suspend fun getReadingProgress(bookId: String): ReadingProgress?
    suspend fun saveReadingProgress(bookId: String, currentPage: Int): ReadingProgress
}
