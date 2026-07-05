package com.example.feedbook.features.library.data.repository

import com.example.feedbook.features.library.data.mapper.toDomain
import com.example.feedbook.features.library.data.remote.LibraryRemoteDataSource
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.library.domain.model.ReaderLibrary
import com.example.feedbook.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(
    private val remoteDataSource: LibraryRemoteDataSource,
    private val refreshBus: UserContentRefreshBus
) : LibraryRepository {
    override fun observeOwnLibrary(): Flow<ReaderLibrary> =
        remoteDataSource.observeOwnLibrary().map { it.toDomain() }

    override suspend fun addBookToLibrary(bookId: String) {
        remoteDataSource.addBookToLibrary(bookId)
        refreshBus.refresh()
    }

    override suspend fun removeBookFromLibrary(bookId: String) {
        remoteDataSource.removeBookFromLibrary(bookId)
        refreshBus.refresh()
    }
}
