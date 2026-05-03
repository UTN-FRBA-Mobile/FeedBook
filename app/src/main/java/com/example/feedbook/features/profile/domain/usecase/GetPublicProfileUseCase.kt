package com.example.feedbook.features.profile.domain.usecase

import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.repository.ProfileRepository

class GetPublicProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): ReaderProfile = repository.getPublicProfile()
}
