package com.example.feedbook.features.profile.domain.usecase

import com.example.feedbook.features.profile.domain.repository.ProfileRepository
import com.example.feedbook.core.network.FollowToggleResponseDto

class ToggleUserFollowUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): FollowToggleResponseDto =
        repository.toggleUserFollow(userId)
}
