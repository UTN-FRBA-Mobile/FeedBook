package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetExploreUsersUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): List<ExploreUser> = repository.getExploreUsers()
}
