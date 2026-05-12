package com.example.feedbook.features.home.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRemoteDataSource(
    private val apiService: ApiService
) {
    fun observeHomeFeed(): Flow<HomeDto> = flow {
        emit(apiService.getHomeFeed())
    }
}
