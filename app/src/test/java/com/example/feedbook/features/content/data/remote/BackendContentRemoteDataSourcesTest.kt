package com.example.feedbook.features.content.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.authors.data.remote.AuthorRemoteDataSource
import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.home.data.remote.HomeRemoteDataSource
import com.example.feedbook.features.home.data.remote.dto.HomeCuratorDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.home.data.remote.dto.HomeFeaturedBookDto
import com.example.feedbook.features.home.data.remote.dto.HomeRankedBookDto
import com.example.feedbook.features.home.data.remote.dto.HomeReadingRoomDto
import com.example.feedbook.features.library.data.remote.LibraryRemoteDataSource
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.library.data.remote.dto.ReadBookDto
import com.example.feedbook.features.notifications.data.remote.NotificationsRemoteDataSource
import com.example.feedbook.features.notifications.data.remote.dto.NotificationActorDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationEntryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
import com.example.feedbook.features.profile.data.remote.dto.CurrentBookDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.feedbook.features.stats.data.remote.StatsRemoteDataSource
import com.example.feedbook.features.stats.data.remote.dto.RadarSectionDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class BackendContentRemoteDataSourcesTest {
    @Test
    fun `author remote data source delegates toggle and detail calls to backend`() = runBlocking {
        val apiService = ContentStubApiService().apply {
            author = AuthorDto(
                id = "author_1",
                name = "Octavia Butler",
                birthYear = 1947,
                deathYear = 2006,
                nationality = "American",
                description = "Sci-fi icon",
                biography = "Biography",
                imageUrl = null,
                books = emptyList(),
                followers = 42
            )
        }
        val dataSource = AuthorRemoteDataSource(apiService)

        val author = dataSource.getAuthorById("author_1")
        dataSource.toggleFollow("author_1")

        assertEquals("Octavia Butler", author.name)
        assertEquals("author_1", apiService.toggledAuthorId)
    }

    @Test
    fun `home remote data source emits backend snapshot`() = runBlocking {
        val dataSource = HomeRemoteDataSource(
            ContentStubApiService().apply {
                home = HomeDto(
                    trendingTitle = "Trending",
                    avatar = AvatarDto(1L, 2L, null, null, null),
                    featuredBook = HomeFeaturedBookDto("Featured", "Piranesi", "Susanna Clarke", null),
                    rankedBooks = listOf(HomeRankedBookDto("01", "Piranesi", "Susanna Clarke", null)),
                    readingRooms = listOf(HomeReadingRoomDto("Lila", null, "Fantasy", "20 readers")),
                    curators = listOf(HomeCuratorDto("Noah", "Speculative fiction", null))
                )
            }
        )

        val home = dataSource.observeHomeFeed().first()

        assertEquals("Trending", home.trendingTitle)
        assertEquals("Piranesi", home.featuredBook.title)
    }

    @Test
    fun `library remote data source emits backend snapshot`() = runBlocking {
        val dataSource = LibraryRemoteDataSource(
            ContentStubApiService().apply {
                library = LibraryDto(
                    title = "My Library",
                    subtitle = "Snapshot",
                    avatar = AvatarDto(1L, 2L, null, null, null),
                    currentBook = CurrentBookDto("1", "Book", "Author", 10, 100, 0.1f, null),
                    readingBooks = emptyList(),
                    shelfBooks = emptyList(),
                    completedBooks = 3,
                    readHistory = listOf(ReadBookDto("Read", "Author", "Jan", "Feb", 5, 0xFF0000))
                )
            }
        )

        val library = dataSource.observeOwnLibrary().first()

        assertEquals("My Library", library.title)
        assertEquals(3, library.completedBooks)
    }

    @Test
    fun `stats and notifications remote data sources read backend payloads`() = runBlocking {
        val apiService = ContentStubApiService().apply {
            stats = StatsDto(
                title = "Reading Ledger",
                subtitle = "Snapshot",
                metrics = emptyList(),
                heatmapMonths = emptyList(),
                heatmapRows = emptyList(),
                heatmapValues = emptyList(),
                radarSections = listOf(RadarSectionDto(mode = "Genre", axes = emptyList(), ranking = emptyList()))
            )
            notifications = NotificationsDto(
                title = "Inbox",
                items = listOf(
                    NotificationEntryDto(
                        id = "notif_1",
                        type = "follow",
                        timestamp = "now",
                        actor = NotificationActorDto("Mila", 1L, 2L),
                        book = null,
                        fallbackText = "Mila followed you"
                    )
                )
            )
        }

        val stats = StatsRemoteDataSource(apiService).getStats()
        val notifications = NotificationsRemoteDataSource(apiService).getNotifications()

        assertEquals("Reading Ledger", stats.title)
        assertEquals("notif_1", notifications.items.first().id)
    }
}

private class ContentStubApiService : ApiService {
    var author: AuthorDto? = null
    var home: HomeDto? = null
    var library: LibraryDto? = null
    var stats: StatsDto? = null
    var notifications: NotificationsDto? = null
    var toggledAuthorId: String? = null

    override suspend fun getBooks(): List<BookDto> = error("unused")

    override suspend fun getBookById(id: String): BookDto = error("unused")

    override suspend fun getExploreUsers(): List<ExploreUserDto> = error("unused")

    override suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = error("unused")

    override suspend fun getReviews(bookId: String): List<ReviewDto> = error("unused")

    override suspend fun getAuthors(): List<AuthorDto> = listOfNotNull(author)

    override suspend fun getAuthorById(id: String): AuthorDto = author ?: error("author not configured")

    override suspend fun toggleFollow(id: String) {
        toggledAuthorId = id
    }

    override suspend fun getHomeFeed(): HomeDto = home ?: error("home not configured")

    override suspend fun getOwnLibrary(): LibraryDto = library ?: error("library not configured")

    override suspend fun getOwnProfile(): ProfileDto = error("unused")

    override suspend fun getOwnPublicProfilePreview(): ProfileDto = error("unused")

    override suspend fun getPublicProfile(): ProfileDto = error("unused")

    override suspend fun getStats(): StatsDto = stats ?: error("stats not configured")

    override suspend fun getNotifications(): NotificationsDto =
        notifications ?: error("notifications not configured")

    override suspend fun updateOwnProfile(body: UpdateProfileRequestDto): ProfileDto = error("unused")
}
