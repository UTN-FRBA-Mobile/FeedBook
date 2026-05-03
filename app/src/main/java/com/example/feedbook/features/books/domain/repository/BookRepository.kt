package com.example.feedbook.features.books.domain.repository

import com.example.feedbook.features.books.domain.model.Book

interface BookRepository {
    suspend fun getBooks(): List<Book>
    suspend fun getBookById(bookId: String): Book
}
