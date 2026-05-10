package com.example.feedbook.features.library.data.repository

import com.example.feedbook.features.library.data.mapper.toDomain
import com.example.feedbook.features.library.data.remote.LibraryRemoteDataSource
import com.example.feedbook.features.library.domain.model.ReaderLibrary
import com.example.feedbook.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(
    private val remoteDataSource: LibraryRemoteDataSource
) : LibraryRepository {
    override fun observeOwnLibrary(): Flow<ReaderLibrary> =
        remoteDataSource.observeOwnLibrary().map { it.toDomain() }
}
