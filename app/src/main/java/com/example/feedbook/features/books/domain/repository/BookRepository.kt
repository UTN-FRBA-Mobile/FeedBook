package com.example.feedbook.features.books.domain.repository

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(bookId: String): Book
    suspend fun getReviews(bookId: String): List<Review>
    suspend fun getReadingProgress(bookId: String): ReadingProgress?
}