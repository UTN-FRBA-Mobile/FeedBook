package com.example.feedbook.features.library.domain.repository

import com.example.feedbook.features.library.domain.model.ReaderLibrary
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun observeOwnLibrary(): Flow<ReaderLibrary>
    suspend fun addBookToLibrary(bookId: String)
    suspend fun removeBookFromLibrary(bookId: String)
}
