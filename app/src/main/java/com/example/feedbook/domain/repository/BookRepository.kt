package com.example.feedbook.domain.repository

import com.example.feedbook.domain.model.Book

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(bookId: String): Book
}
