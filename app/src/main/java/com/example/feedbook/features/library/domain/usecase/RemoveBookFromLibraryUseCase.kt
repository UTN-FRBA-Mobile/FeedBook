package com.example.feedbook.features.library.domain.usecase

import com.example.feedbook.features.library.domain.repository.LibraryRepository

class RemoveBookFromLibraryUseCase(
    private val repository: LibraryRepository
) {
    suspend operator fun invoke(bookId: String) = repository.removeBookFromLibrary(bookId)
}
