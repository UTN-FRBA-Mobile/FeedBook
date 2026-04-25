package com.example.feedbook.domain.usecase

import com.example.feedbook.domain.model.Book
import com.example.feedbook.domain.repository.BookRepository

class GetBookByIdUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): Book {
        return repository.getBookById(bookId)
    }
}
