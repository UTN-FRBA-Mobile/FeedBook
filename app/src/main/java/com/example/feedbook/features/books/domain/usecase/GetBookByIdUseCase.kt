package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetBookByIdUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Book {
        return repository.getBookById(bookId)
    }
}
