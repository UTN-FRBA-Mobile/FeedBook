package com.example.feedbook.data.repository

import com.example.feedbook.data.mapper.toDomain
import com.example.feedbook.data.remote.BookRemoteDataSource
import com.example.feedbook.domain.model.Book
import com.example.feedbook.domain.repository.BookRepository

class BookRepositoryImpl(
    private val remoteDataSource: BookRemoteDataSource
) : BookRepository {
    override suspend fun getBooks(): List<Book> {
        return remoteDataSource.getBooks().map { dto -> dto.toDomain() }
    }

    override suspend fun getBookById(bookId: String): Book {
        return remoteDataSource.getBookById(bookId).toDomain()
    }
}
