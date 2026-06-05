package com.example.feedbook.features.books.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.data.remote.dto.ReviewsResponseDto
import com.example.feedbook.features.books.data.remote.dto.SaveReviewRequestDto

class BookRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getBooks(): List<BookDto> = apiService.getBooks()

    suspend fun getBookById(bookId: String): BookDto = apiService.getBookById(bookId)

    suspend fun getExploreUsers(): List<ExploreUserDto> = apiService.getExploreUsers()

    suspend fun getReadingProgress(bookId: String): ReadingProgressDto? =
        apiService.getReadingProgress(bookId)

    suspend fun saveReadingProgress(bookId: String, currentPage: Int): ReadingProgressDto =
        apiService.updateReadingProgress(bookId, mapOf("current_page" to currentPage))

    suspend fun getReviews(bookId: String, page: Int = 1, limit: Int = 5): ReviewsResponseDto =
        apiService.getReviews(bookId, page, limit)

    suspend fun saveReview(bookId: String, rating: Float, text: String): ReviewDto =
        apiService.saveReview(bookId, SaveReviewRequestDto(rating, text))

    suspend fun toggleLike(bookId: String, reviewId: String): ReviewDto =
        apiService.toggleLike(bookId, reviewId)
}
