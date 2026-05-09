package com.example.feedbook.features.books.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.books.data.remote.dto.BookDto

class BookRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getBooks(): List<BookDto> = apiService.getBooks()

    suspend fun getBookById(bookId: String): BookDto = apiService.getBookById(bookId)
}
