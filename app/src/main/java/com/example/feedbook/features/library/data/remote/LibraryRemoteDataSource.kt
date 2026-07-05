package com.example.feedbook.features.library.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class LibraryRemoteDataSource(
    private val apiService: ApiService,
    private val refreshBus: UserContentRefreshBus
) {
    fun observeOwnLibrary(): Flow<LibraryDto> =
        refreshBus.version.flatMapLatest { flowOf(apiService.getOwnLibrary()) }

    suspend fun addBookToLibrary(bookId: String) {
        apiService.addBookToLibrary(mapOf("book_id" to bookId))
    }

    suspend fun removeBookFromLibrary(bookId: String) {
        apiService.removeBookFromLibrary(mapOf("book_id" to bookId))
    }
}
