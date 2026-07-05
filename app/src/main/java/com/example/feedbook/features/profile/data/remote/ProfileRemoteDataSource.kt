package com.example.feedbook.features.profile.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.core.state.UserContentRefreshBus
import okhttp3.MultipartBody
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarUploadResponseDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ProfileRemoteDataSource(
    private val apiService: ApiService,
    private val refreshBus: UserContentRefreshBus
) {
    fun observeOwnProfile(): Flow<ProfileDto> =
        refreshBus.version.flatMapLatest { flowOf(apiService.getOwnProfile()) }

    fun observeOwnPublicPreview(): Flow<ProfileDto> =
        refreshBus.version.flatMapLatest { flowOf(apiService.getOwnPublicProfilePreview()) }

    suspend fun getPublicProfile(userId: String): ProfileDto = apiService.getPublicProfile(userId)

    suspend fun uploadOwnAvatar(image: MultipartBody.Part): AvatarUploadResponseDto =
        apiService.uploadOwnAvatar(image)

    suspend fun updateOwnProfile(request: UpdateProfileRequestDto): ProfileDto {
        val updatedProfile = apiService.updateOwnProfile(request)
        refreshBus.refresh()
        return updatedProfile
    }

    suspend fun toggleUserFollow(userId: String) {
        apiService.toggleUserFollow(userId)
    }
}
