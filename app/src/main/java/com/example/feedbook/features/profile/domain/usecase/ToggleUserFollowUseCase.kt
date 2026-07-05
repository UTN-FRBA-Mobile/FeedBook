package com.example.feedbook.features.profile.domain.usecase

import com.example.feedbook.features.profile.domain.repository.ProfileRepository

class ToggleUserFollowUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.toggleUserFollow(userId)
    }
}
