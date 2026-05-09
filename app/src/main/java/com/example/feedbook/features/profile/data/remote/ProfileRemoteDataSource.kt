package com.example.feedbook.features.profile.data.remote

import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend
import kotlinx.coroutines.flow.Flow

class ProfileRemoteDataSource(
    private val fakeBackend: FakeFeedBookBackend
) {
    fun observeOwnProfile(): Flow<ProfileDto> = fakeBackend.observeOwnProfile()

    fun observeOwnPublicPreview(): Flow<ProfileDto> = fakeBackend.observeOwnPublicPreview()

    suspend fun getPublicProfile(): ProfileDto = fakeBackend.getPublicProfile()

    suspend fun updateOwnProfile(request: UpdateProfileRequestDto) {
        fakeBackend.updateOwnProfile(request)
    }
}
