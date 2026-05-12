package com.example.feedbook.features.books.data.repository

import com.example.feedbook.features.books.data.mapper.toDomain
import com.example.feedbook.features.books.data.remote.BookRemoteDataSource
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.repository.BookRepository

class BookRepositoryImpl(
    private val remoteDataSource: BookRemoteDataSource,
) : BookRepository {
    override suspend fun getBooks(): List<Book> {
        return remoteDataSource.getBooks().map { dto -> dto.toDomain() }
    }

    override suspend fun getExploreUsers(): List<ExploreUser> {
        return remoteDataSource.getExploreUsers().map { dto -> dto.toDomain() }
    }

    override suspend fun getBookById(bookId: String): Book {
        return remoteDataSource.getBookById(bookId).toDomain()
    }

    override suspend fun getReviews(bookId: String): List<Review> {
        return remoteDataSource.getReviews(bookId)
            .map { it.toDomain() }
    }

    override suspend fun getReadingProgress(bookId: String): ReadingProgress? {
        return remoteDataSource.getReadingProgress(bookId)?.toDomain()
    }
}
