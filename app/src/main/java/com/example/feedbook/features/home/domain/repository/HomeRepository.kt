package com.example.feedbook.features.home.domain.repository

import com.example.feedbook.features.home.domain.model.HomeFeed
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun observeHomeFeed(): Flow<HomeFeed>
}
