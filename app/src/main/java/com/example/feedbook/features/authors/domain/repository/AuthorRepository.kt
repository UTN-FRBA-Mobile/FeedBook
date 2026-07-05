package com.example.feedbook.features.authors.domain.repository

import com.example.feedbook.features.authors.domain.model.Author

interface AuthorRepository {
    suspend fun getAuthors(): List<Author>
    suspend fun getAuthorById(authorId: String): Author

    suspend fun toggleFollow(authorId: String)
}
