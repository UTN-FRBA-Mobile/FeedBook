package com.example.feedbook.features.profile.data.repository

import com.example.feedbook.features.profile.data.mapper.toDomain
import com.example.feedbook.features.profile.data.mapper.toDto
import com.example.feedbook.features.profile.data.remote.ProfileRemoteDataSource
import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import com.example.feedbook.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override fun observeOwnProfile(): Flow<ReaderProfile> =
        remoteDataSource.observeOwnProfile().map { it.toDomain() }

    override fun observeOwnPublicPreview(): Flow<ReaderProfile> =
        remoteDataSource.observeOwnPublicPreview().map { it.toDomain() }

    override suspend fun getPublicProfile(): ReaderProfile =
        remoteDataSource.getPublicProfile().toDomain()

    override suspend fun updateOwnProfile(command: UpdateProfileCommand) {
        remoteDataSource.updateOwnProfile(command.toDto())
    }
}
