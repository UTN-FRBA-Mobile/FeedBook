package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetBookByIsbnUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(isbn: String): Book {
        return repository.getBookByIsbn(isbn)
    }
}
