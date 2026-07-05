package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.ExploreSearchResults
import com.example.feedbook.features.books.domain.repository.BookRepository

class SearchExploreUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(query: String): ExploreSearchResults =
        repository.search(query)
}
