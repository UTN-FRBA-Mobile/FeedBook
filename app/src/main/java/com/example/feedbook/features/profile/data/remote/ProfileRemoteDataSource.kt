package com.example.feedbook.features.profile.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart

class ProfileRemoteDataSource(
    private val apiService: ApiService
) {
    private val ownProfileState = MutableStateFlow<ProfileDto?>(null)
    private val ownPublicPreviewState = MutableStateFlow<ProfileDto?>(null)

    fun observeOwnProfile(): Flow<ProfileDto> =
        ownProfileState.filterNotNull().onStart {
            if (ownProfileState.value == null) {
                ownProfileState.value = apiService.getOwnProfile()
            }
        }

    fun observeOwnPublicPreview(): Flow<ProfileDto> =
        ownPublicPreviewState.filterNotNull().onStart {
            if (ownPublicPreviewState.value == null) {
                ownPublicPreviewState.value = apiService.getOwnPublicProfilePreview()
            }
        }

    suspend fun getPublicProfile(): ProfileDto = apiService.getPublicProfile()

    suspend fun updateOwnProfile(request: UpdateProfileRequestDto): ProfileDto {
        val updatedProfile = apiService.updateOwnProfile(request)
        ownProfileState.value = updatedProfile
        ownPublicPreviewState.value = apiService.getOwnPublicProfilePreview()
        return updatedProfile
    }
}
