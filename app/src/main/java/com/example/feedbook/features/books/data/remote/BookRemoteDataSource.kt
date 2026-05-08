package com.example.feedbook.features.books.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto

class BookRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getBooks(): List<BookDto> = apiService.getBooks()

    suspend fun getBookById(bookId: String): BookDto = apiService.getBookById(bookId)

    suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = apiService.getReadingProgress(bookId)

    suspend fun getReviews(bookId: String): List<ReviewDto> = apiService.getReviews(bookId)
}
