package com.example.feedbook.features.authors.data.repository

import com.example.feedbook.features.authors.data.mapper.toDomain
import com.example.feedbook.features.authors.data.remote.AuthorRemoteDataSource
import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.authors.domain.repository.AuthorRepository

class AuthorRepositoryImpl(
    private val remoteDataSource: AuthorRemoteDataSource,
) : AuthorRepository {

    override suspend fun getAuthors(): List<Author> {
        return remoteDataSource.getAuthors().map { it.toDomain() }
    }

    override suspend fun getAuthorById(authorId: String): Author {
        return remoteDataSource.getAuthorById(authorId).toDomain()
    }

    override suspend fun toggleFollow(authorId: String) {
        remoteDataSource.toggleFollow(authorId)
    }

}
