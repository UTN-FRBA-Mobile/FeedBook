package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetAuthorUsersUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(authorId: String): List<ExploreUser> = repository.getAuthorUsers(authorId)
}
