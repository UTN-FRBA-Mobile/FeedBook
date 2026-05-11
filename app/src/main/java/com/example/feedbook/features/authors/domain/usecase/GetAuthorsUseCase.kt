package com.example.feedbook.features.authors.domain.usecase

import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.authors.domain.repository.AuthorRepository

class GetAuthorsUseCase (
    private val repository: AuthorRepository
) {
    suspend operator fun invoke(): List<Author> {
        return repository.getAuthors()
    }
}