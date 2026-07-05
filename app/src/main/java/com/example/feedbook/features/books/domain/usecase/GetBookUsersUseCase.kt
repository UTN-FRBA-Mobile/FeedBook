package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetBookUsersUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): List<ExploreUser> = repository.getBookUsers(bookId)
}
