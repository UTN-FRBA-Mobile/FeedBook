package com.example.feedbook.features.books.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.core.network.RegisterPushTokenRequestDto
import com.example.feedbook.core.network.UnlinkPushTokenRequestDto
import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.FriendReadingDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.data.remote.dto.ReviewsResponseDto
import com.example.feedbook.features.books.data.remote.dto.SaveReviewRequestDto
import com.example.feedbook.features.books.data.remote.dto.SearchResponseDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.profile.data.remote.dto.AvatarUploadResponseDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ChangeReadingRoomBookRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.CreateReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.DeleteReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomDetailDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomListDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomCommentRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomRatingRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.UpdateReadingRoomDescriptionRequestDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class BookRemoteDataSourceTest {
    @Test
    fun `get explore users returns backend users`() = runBlocking {
        val dataSource = BookRemoteDataSource(
            apiService = object : StubApiService() {
                override suspend fun getExploreUsers(): List<ExploreUserDto> = listOf(
                    ExploreUserDto(
                        id = "user_1",
                        name = "Reader",
                        handle = "@reader",
                        bio = "bio",
                        avatarImageUrl = null,
                        avatarTopColorHex = 1,
                        avatarBottomColorHex = 2,
                        followersLabel = "1",
                        booksReadLabel = "2"
                    )
                )
            }
        )

        val users = dataSource.getExploreUsers()

        assertEquals(1, users.size)
        assertEquals("Reader", users.first().name)
    }

    @Test
    fun `search query is delegated to backend`() = runBlocking {
        val apiService = object : StubApiService() {
            var query: String? = null

            override suspend fun search(query: String): SearchResponseDto {
                this.query = query
                return SearchResponseDto()
            }
        }
        val dataSource = BookRemoteDataSource(apiService)

        dataSource.search("  dune  ")

        assertEquals("dune", apiService.query)
    }
}

private open class StubApiService : ApiService {
    override suspend fun getBooks(): List<BookDto> = error("unused")
    override suspend fun getBookById(id: String): BookDto = error("unused")
    override suspend fun getBookByIsbn(isbn: String): BookDto = error("unused")
    override suspend fun getExploreUsers(): List<ExploreUserDto> = error("unused")
    override suspend fun getExploreUserById(id: String): ExploreUserDto = error("unused")
    override suspend fun search(query: String): SearchResponseDto = error("unused")
    override suspend fun getReadingProgress(bookId: String): ReadingProgressDto? = error("unused")
    override suspend fun getReviews(bookId: String, page: Int, limit: Int): ReviewsResponseDto =
        error("unused")

    override suspend fun updateReadingProgress(
        bookId: String,
        body: Map<String, Int>
    ): ReadingProgressDto = error("unused")

    override suspend fun saveReview(
        bookId: String,
        body: SaveReviewRequestDto
    ): ReviewDto = error("unused")

    override suspend fun toggleLike(bookId: String, reviewId: String): ReviewDto = error("unused")
    override suspend fun getAuthors(): List<AuthorDto> = error("unused")
    override suspend fun getAuthorById(id: String): AuthorDto = error("unused")
    override suspend fun toggleFollow(id: String) = error("unused")
    override suspend fun toggleUserFollow(userId: String) = error("unused")
    override suspend fun getFriendsReading(bookId: String): List<FriendReadingDto> = error("unused")
    override suspend fun getHomeFeed(): HomeDto = error("unused")
    override suspend fun getReadingRooms(): ReadingRoomListDto = error("unused")
    override suspend fun createReadingRoom(body: CreateReadingRoomRequestDto): ReadingRoomDetailDto =
        error("unused")
    override suspend fun getReadingRoom(id: String): ReadingRoomDetailDto = error("unused")
    override suspend fun joinReadingRoom(id: String): ReadingRoomDetailDto = error("unused")
    override suspend fun leaveReadingRoom(id: String) = error("unused")
    override suspend fun updateReadingRoomDescription(
        id: String,
        body: UpdateReadingRoomDescriptionRequestDto
    ): ReadingRoomDetailDto = error("unused")
    override suspend fun deleteReadingRoom(id: String, body: DeleteReadingRoomRequestDto) =
        error("unused")
    override suspend fun kickReadingRoomMember(id: String, userId: String) = error("unused")
    override suspend fun changeReadingRoomBook(
        id: String,
        body: ChangeReadingRoomBookRequestDto
    ): ReadingRoomDetailDto = error("unused")
    override suspend fun saveReadingRoomRating(
        id: String,
        body: SaveReadingRoomRatingRequestDto
    ): ReadingRoomDetailDto = error("unused")
    override suspend fun saveReadingRoomComment(
        id: String,
        body: SaveReadingRoomCommentRequestDto
    ): ReadingRoomDetailDto = error("unused")
    override suspend fun getOwnLibrary(): LibraryDto = error("unused")
    override suspend fun getFollowedBooks(): List<BookDto> = error("unused")
    override suspend fun addBookToLibrary(body: Map<String, String>) = error("unused")
    override suspend fun removeBookFromLibrary(body: Map<String, String>) = error("unused")
    override suspend fun getOwnProfile(): ProfileDto = error("unused")
    override suspend fun getOwnPublicProfilePreview(): ProfileDto = error("unused")
    override suspend fun getPublicProfile(userId: String): ProfileDto = error("unused")
    override suspend fun getStats(): StatsDto = error("unused")
    override suspend fun getNotifications(): NotificationsDto = error("unused")
    override suspend fun updateOwnProfile(body: UpdateProfileRequestDto): ProfileDto = error("unused")
    override suspend fun uploadOwnAvatar(image: okhttp3.MultipartBody.Part): AvatarUploadResponseDto =
        error("unused")
    override suspend fun registerPushToken(body: RegisterPushTokenRequestDto) = error("unused")
    override suspend fun unlinkPushToken(body: UnlinkPushTokenRequestDto) = error("unused")
}
