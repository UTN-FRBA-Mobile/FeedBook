package com.example.feedbook.features.profile.domain.repository

import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeOwnProfile(): Flow<ReaderProfile>
    fun observeOwnPublicPreview(): Flow<ReaderProfile>
    suspend fun getPublicProfile(): ReaderProfile
    suspend fun updateOwnProfile(command: UpdateProfileCommand)
}
