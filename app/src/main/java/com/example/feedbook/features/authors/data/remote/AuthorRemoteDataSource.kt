package com.example.feedbook.features.authors.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
class AuthorRemoteDataSource (
    private val apiService: ApiService
) {
    suspend fun getAuthors(): List<AuthorDto> = apiService.getAuthors()

    suspend fun getAuthorById(authorId: String): AuthorDto = apiService.getAuthorById(authorId)
    suspend fun toggleFollow(authorId: String): Void = apiService.toggleFollow(authorId)
}