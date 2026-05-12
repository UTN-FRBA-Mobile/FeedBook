package com.example.feedbook.features.profile.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
import com.example.feedbook.features.profile.data.remote.dto.CurrentBookDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileRemoteDataSourceTest {
    @Test
    fun `update own profile refreshes observed cached state`() = runBlocking {
        val initial = sampleProfile(name = "Before")
        val updated = sampleProfile(name = "After")

        val apiService = object : ApiService {
            override suspend fun getOwnProfile(): ProfileDto = initial
            override suspend fun getOwnPublicProfilePreview(): ProfileDto = updated
            override suspend fun updateOwnProfile(body: UpdateProfileRequestDto): ProfileDto = updated
            override suspend fun getPublicProfile(): ProfileDto = updated
            override suspend fun getBooks(): List<BookDto> = error("unused")
            override suspend fun getBookById(id: String): BookDto = error("unused")
            override suspend fun getExploreUsers(): List<ExploreUserDto> = error("unused")
            override suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = error("unused")
            override suspend fun getReviews(bookId: String): List<ReviewDto> = error("unused")
            override suspend fun getAuthors(): List<AuthorDto> = error("unused")
            override suspend fun getAuthorById(id: String): AuthorDto = error("unused")
            override suspend fun toggleFollow(id: String) = error("unused")
            override suspend fun getHomeFeed(): HomeDto = error("unused")
            override suspend fun getOwnLibrary(): LibraryDto = error("unused")
            override suspend fun getStats(): StatsDto = error("unused")
            override suspend fun getNotifications(): NotificationsDto = error("unused")
        }

        val dataSource = ProfileRemoteDataSource(apiService)
        assertEquals("Before", dataSource.observeOwnProfile().first().name)

        dataSource.updateOwnProfile(
            UpdateProfileRequestDto(
                name = "After",
                handle = "@after",
                quote = "Updated",
                avatarTopColorHex = 1L,
                avatarBottomColorHex = 2L,
                avatarPresetId = null,
                avatarImageUri = null,
                targetPagesPerDay = 10
            )
        )

        assertEquals("After", dataSource.observeOwnProfile().first().name)
        assertEquals("After", dataSource.observeOwnPublicPreview().first().name)
    }

    private fun sampleProfile(name: String) = ProfileDto(
        name = name,
        handle = "@sample",
        quote = "quote",
        avatar = AvatarDto(1L, 2L, null, null, null),
        availableAvatarPresets = emptyList(),
        readingGoal = null,
        readingStreak = com.example.feedbook.features.profile.data.remote.dto.ReadingStreakDto(0, emptyList()),
        currentBook = CurrentBookDto("1", "Book", "Author", 1, 10, 0.1f, null),
        upNextBooks = emptyList(),
        completedBooks = 0,
        profileStats = emptyList(),
        publicLibrary = emptyList(),
        featuredReviews = emptyList()
    )
}
