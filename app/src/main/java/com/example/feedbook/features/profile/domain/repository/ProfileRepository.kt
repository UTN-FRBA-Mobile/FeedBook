package com.example.feedbook.features.profile.domain.repository

import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeOwnProfile(): Flow<ReaderProfile>
    fun observeOwnPublicPreview(): Flow<ReaderProfile>
    suspend fun getPublicProfile(userId: String): ReaderProfile
    suspend fun updateOwnProfile(command: UpdateProfileCommand)
    suspend fun toggleUserFollow(userId: String)
}
