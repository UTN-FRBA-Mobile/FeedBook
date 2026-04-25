package com.example.feedbook.domain.usecase

import com.example.feedbook.domain.model.Book
import com.example.feedbook.domain.repository.BookRepository

class GetBooksUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): List<Book> {
        return repository.getBooks()
    }
}
