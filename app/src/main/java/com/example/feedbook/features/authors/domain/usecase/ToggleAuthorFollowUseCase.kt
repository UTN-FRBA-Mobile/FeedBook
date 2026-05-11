package com.example.feedbook.features.authors.domain.usecase

import com.example.feedbook.features.authors.domain.repository.AuthorRepository

class ToggleAuthorFollowUseCase (
        private val repository: AuthorRepository
) {
    suspend operator fun invoke(authorId: String) {
        return repository.toggleFollow(authorId)
    }
}