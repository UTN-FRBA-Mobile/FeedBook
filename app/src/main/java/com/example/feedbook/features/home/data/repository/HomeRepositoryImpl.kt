package com.example.feedbook.features.home.data.repository

import com.example.feedbook.features.home.data.mapper.toDomain
import com.example.feedbook.features.home.data.remote.HomeRemoteDataSource
import com.example.feedbook.features.home.domain.model.HomeFeed
import com.example.feedbook.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val remoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    override fun observeHomeFeed(): Flow<HomeFeed> =
        remoteDataSource.observeHomeFeed().map { it.toDomain() }
}
