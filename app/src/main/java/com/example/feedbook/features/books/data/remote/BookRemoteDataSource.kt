package com.example.feedbook.features.books.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class BookRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
//    private val apiService: ApiService
) {
//    suspend fun getBooks(): List<BookDto> = apiService.getBooks()
    suspend fun getBooks(): List<BookDto> = fakeBackend.getBooks()

//    suspend fun getBookById(bookId: String): BookDto = apiService.getBookById(bookId)
    suspend fun getBookById(bookId: String): BookDto = fakeBackend.getBookById(bookId)

    suspend fun getExploreUsers(): List<ExploreUserDto> = fakeBackend.getExploreUsers()

//    suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = apiService.getReadingProgress(bookId)
    suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = fakeBackend.getReadingProgress(bookId)

//    suspend fun getReviews(bookId: String): List<ReviewDto> = apiService.getReviews(bookId)
    suspend fun getReviews(bookId: String): List<ReviewDto> = fakeBackend.getReviews(bookId)

}
