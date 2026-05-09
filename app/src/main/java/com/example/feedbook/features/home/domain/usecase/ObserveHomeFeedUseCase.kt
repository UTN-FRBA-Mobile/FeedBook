package com.example.feedbook.features.home.domain.usecase

import com.example.feedbook.features.home.domain.repository.HomeRepository

class ObserveHomeFeedUseCase(
    private val repository: HomeRepository
) {
    operator fun invoke() = repository.observeHomeFeed()
}
