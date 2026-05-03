package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetBooksUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(): List<Book> {
        return repository.getBooks()
    }
}
