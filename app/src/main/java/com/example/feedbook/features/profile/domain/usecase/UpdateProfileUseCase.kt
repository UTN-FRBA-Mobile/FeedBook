package com.example.feedbook.features.profile.domain.usecase

import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import com.example.feedbook.features.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(command: UpdateProfileCommand) {
        repository.updateOwnProfile(command)
    }
}
