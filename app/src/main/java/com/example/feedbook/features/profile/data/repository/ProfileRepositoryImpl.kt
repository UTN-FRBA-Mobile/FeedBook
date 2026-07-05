package com.example.feedbook.features.profile.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.feedbook.features.profile.data.mapper.toDomain
import com.example.feedbook.features.profile.data.mapper.toDto
import com.example.feedbook.features.profile.data.remote.ProfileRemoteDataSource
import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import com.example.feedbook.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale

class ProfileRepositoryImpl(
    private val context: Context,
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override fun observeOwnProfile(): Flow<ReaderProfile> =
        remoteDataSource.observeOwnProfile().map { it.toDomain() }

    override fun observeOwnPublicPreview(): Flow<ReaderProfile> =
        remoteDataSource.observeOwnPublicPreview().map { it.toDomain() }

    override suspend fun getPublicProfile(userId: String): ReaderProfile =
        remoteDataSource.getPublicProfile(userId).toDomain()

    override suspend fun toggleUserFollow(userId: String) {
        remoteDataSource.toggleUserFollow(userId)
    }

    override suspend fun updateOwnProfile(command: UpdateProfileCommand) {
        val avatarImageUri = command.avatarImageUri
            ?.takeIf { it.isNotBlank() }
            ?.let { uploadAvatarIfNeeded(it) }

        remoteDataSource.updateOwnProfile(command.copy(avatarImageUri = avatarImageUri).toDto())
    }

    private suspend fun uploadAvatarIfNeeded(rawUri: String): String {
        if (rawUri.startsWith("http://", ignoreCase = true) || rawUri.startsWith("https://", ignoreCase = true)) {
            return rawUri
        }

        val uri = Uri.parse(rawUri)
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Unable to read selected avatar image")

        val bytes = inputStream.use { it.readBytes() }
        if (bytes.isEmpty()) {
            throw IllegalStateException("Selected avatar image is empty")
        }

        val mimeType = context.contentResolver.getType(uri)
        val requestBody = bytes.toRequestBody(
            mimeType?.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()
        )
        val fileName = buildAvatarFileName(uri, mimeType)
        val part = MultipartBody.Part.createFormData("image", fileName, requestBody)
        return remoteDataSource.uploadOwnAvatar(part).avatarImageUrl
    }

    private fun buildAvatarFileName(uri: Uri, mimeType: String?): String {
        val extension = mimeType
            ?.let(MimeTypeMap.getSingleton()::getExtensionFromMimeType)
            ?.takeIf { it.isNotBlank() }
            ?: uri.lastPathSegment
                ?.substringAfterLast('.')
                ?.takeIf { it.isNotBlank() }
                ?.lowercase(Locale.US)
                ?: "bin"
        return "avatar-${System.currentTimeMillis()}.$extension"
    }
}
