package com.example.feedbook.features.authors.domain.usecase

import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.authors.domain.repository.AuthorRepository

class GetAuthorByIdUseCase(
    private val repository: AuthorRepository
) {
    suspend operator fun invoke(authorId: String): Author {
        return repository.getAuthorById(authorId)
    }
}