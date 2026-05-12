package com.example.feedbook.features.library.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LibraryRemoteDataSource(
    private val apiService: ApiService
) {
    fun observeOwnLibrary(): Flow<LibraryDto> = flow {
        emit(apiService.getOwnLibrary())
    }
}
