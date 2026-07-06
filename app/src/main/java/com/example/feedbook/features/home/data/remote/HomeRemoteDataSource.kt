package com.example.feedbook.features.home.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class HomeRemoteDataSource(
    private val apiService: ApiService,
    private val refreshBus: UserContentRefreshBus
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeHomeFeed(): Flow<HomeDto> =
        refreshBus.version.flatMapLatest { flowOf(apiService.getHomeFeed()) }
}
