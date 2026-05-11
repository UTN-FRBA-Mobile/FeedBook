package com.example.feedbook.features.authors.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class AuthorRemoteDataSource (
//    private val apiService: ApiService
    private val fakeBackend: FakeFeedBookBackend
) {
//    suspend fun getAuthors(): List<AuthorDto> = apiService.getAuthors()
    suspend fun getAuthors(): List<AuthorDto> = fakeBackend.getAuthors()

//    suspend fun getAuthorById(authorId: String): AuthorDto = apiService.getAuthorById(authorId)
    suspend fun getAuthorById(authorId: String): AuthorDto = fakeBackend.getAuthorById(authorId)

//    suspend fun toggleFollow(authorId: String): Void = apiService.toggleFollow(authorId)
    suspend fun toggleFollow(authorId: String): Unit = fakeBackend.toggleFollow(authorId)

}