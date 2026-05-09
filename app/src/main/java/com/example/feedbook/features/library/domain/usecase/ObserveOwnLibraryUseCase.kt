package com.example.feedbook.features.library.domain.usecase

import com.example.feedbook.features.library.domain.repository.LibraryRepository

class ObserveOwnLibraryUseCase(
    private val repository: LibraryRepository
) {
    operator fun invoke() = repository.observeOwnLibrary()
}
