package com.example.feedbook.core.network

import com.example.feedbook.data.remote.dto.BookDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDto
}
