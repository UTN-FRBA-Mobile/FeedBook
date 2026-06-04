package com.example.feedbook.features.library.domain.usecase

import com.example.feedbook.features.library.domain.repository.LibraryRepository

class AddBookToLibraryUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(bookId: String) = repository.addBookToLibrary(bookId)
}
