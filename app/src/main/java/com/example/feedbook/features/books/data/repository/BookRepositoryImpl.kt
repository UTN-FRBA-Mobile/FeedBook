package com.example.feedbook.features.books.data.repository

import com.example.feedbook.features.books.data.mapper.toDomain
import com.example.feedbook.features.books.data.remote.BookRemoteDataSource
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.model.ExploreSearchResults
import com.example.feedbook.features.books.domain.model.FriendReading
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.books.domain.model.Review
import com.example.feedbook.features.books.domain.model.ReviewPart
import com.example.feedbook.features.books.domain.repository.BookRepository

class BookRepositoryImpl(
    private val remoteDataSource: BookRemoteDataSource,
    private val refreshBus: UserContentRefreshBus,
) : BookRepository {
    override suspend fun getBooks(): List<Book> {
        return remoteDataSource.getBooks().map { dto -> dto.toDomain() }
    }

    override suspend fun getExploreUsers(): List<ExploreUser> {
        return remoteDataSource.getExploreUsers().map { dto -> dto.toDomain() }
    }

    override suspend fun getExploreUserById(id: String): ExploreUser {
        return remoteDataSource.getExploreUserById(id).toDomain()
    }

    override suspend fun search(query: String): ExploreSearchResults {
        return remoteDataSource.search(query).toDomain()
    }

    override suspend fun getBookById(bookId: String): Book {
        return remoteDataSource.getBookById(bookId).toDomain()
    }

    override suspend fun getBookByIsbn(isbn: String): Book {
        return remoteDataSource.getBookByIsbn(isbn).toDomain()
    }

    override suspend fun getReviews(bookId: String, page: Int, limit: Int): Pair<List<Review>, Int> {
        val response = remoteDataSource.getReviews(bookId, page, limit)
        return response.reviews.map { it.toDomain() } to response.total
    }

    override suspend fun saveReview(bookId: String, rating: Float, text: String, parts: List<ReviewPart>): Review {
        return remoteDataSource.saveReview(bookId, rating, text, parts).toDomain().also {
            refreshBus.refresh()
        }
    }

    override suspend fun getReadingProgress(bookId: String): ReadingProgress? {
        return remoteDataSource.getReadingProgress(bookId)?.toDomain()
    }

    override suspend fun saveReadingProgress(bookId: String, currentPage: Int): ReadingProgress {
        return remoteDataSource.saveReadingProgress(bookId, currentPage).toDomain().also {
            refreshBus.refresh()
        }
    }

    override suspend fun toggleLike(bookId: String, reviewId: String): Review {
        return remoteDataSource.toggleLike(bookId, reviewId).toDomain()
    }

    override suspend fun getFriendsReading(bookId: String): List<FriendReading> {
        return remoteDataSource.getFriendsReading(bookId).map { it.toDomain() }
    }

    override suspend fun getBookUsers(bookId: String): List<ExploreUser> {
        return remoteDataSource.getBookUsers(bookId).map { it.toDomain() }
    }

    override suspend fun getAuthorUsers(authorId: String): List<ExploreUser> {
        return remoteDataSource.getAuthorUsers(authorId).map { it.toDomain() }
    }
}
