package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetUserFollowersUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(userId: String): List<ExploreUser> = repository.getUserFollowers(userId)
}
