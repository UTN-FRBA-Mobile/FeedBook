package com.example.feedbook.features.profile.domain.usecase

import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ObserveOwnProfileUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<ReaderProfile> = repository.observeOwnProfile()
}
