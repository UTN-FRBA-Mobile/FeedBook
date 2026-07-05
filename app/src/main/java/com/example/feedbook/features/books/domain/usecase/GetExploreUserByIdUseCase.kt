package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetExploreUserByIdUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(id: String): ExploreUser = repository.getExploreUserById(id)
}
