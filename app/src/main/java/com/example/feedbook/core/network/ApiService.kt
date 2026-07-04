package com.example.feedbook.core.network

import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.data.remote.dto.ReviewsResponseDto
import com.example.feedbook.features.books.data.remote.dto.SaveReviewRequestDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
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
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDto

    @GET("books/isbn/{isbn}")
    suspend fun getBookByIsbn(@Path("isbn") isbn: String): BookDto

    @GET("explore/users")
    suspend fun getExploreUsers(): List<ExploreUserDto>

    @GET("books/{bookId}/progress")
    suspend fun getReadingProgress(
        @Path("bookId") bookId: String
    ): ReadingProgressDto?

    @PUT("books/{bookId}/progress")
    suspend fun updateReadingProgress(
        @Path("bookId") bookId: String,
        @Body body: Map<String, Int>
    ): ReadingProgressDto

    @GET("books/{bookId}/reviews")
    suspend fun getReviews(
        @Path("bookId") bookId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 5
    ): ReviewsResponseDto

    @POST("books/{bookId}/reviews")
    suspend fun saveReview(
        @Path("bookId") bookId: String,
        @Body body: SaveReviewRequestDto
    ): ReviewDto

    @POST("books/{bookId}/reviews/{reviewId}/like")
    suspend fun toggleLike(
        @Path("bookId") bookId: String,
        @Path("reviewId") reviewId: String
    ): ReviewDto

    @GET("authors")
    suspend fun getAuthors(): List<AuthorDto>

    @GET("authors/{id}")
    suspend fun getAuthorById(@Path("id") id: String): AuthorDto

    @POST("authors/{id}/follow-toggle")
    suspend fun toggleFollow(@Path("id") id: String)

    @GET("home")
    suspend fun getHomeFeed(): HomeDto

    @GET("reading-rooms")
    suspend fun getReadingRooms(): ReadingRoomListDto

    @POST("reading-rooms")
    suspend fun createReadingRoom(@Body body: CreateReadingRoomRequestDto): ReadingRoomDetailDto

    @GET("reading-rooms/{id}")
    suspend fun getReadingRoom(@Path("id") id: String): ReadingRoomDetailDto

    @POST("reading-rooms/{id}/join")
    suspend fun joinReadingRoom(@Path("id") id: String): ReadingRoomDetailDto

    @POST("reading-rooms/{id}/leave")
    suspend fun leaveReadingRoom(@Path("id") id: String)

    @PUT("reading-rooms/{id}/description")
    suspend fun updateReadingRoomDescription(
        @Path("id") id: String,
        @Body body: UpdateReadingRoomDescriptionRequestDto
    ): ReadingRoomDetailDto

    @HTTP(method = "DELETE", path = "reading-rooms/{id}", hasBody = true)
    suspend fun deleteReadingRoom(
        @Path("id") id: String,
        @Body body: DeleteReadingRoomRequestDto
    )

    @HTTP(method = "DELETE", path = "reading-rooms/{id}/members/{userId}/kick")
    suspend fun kickReadingRoomMember(
        @Path("id") id: String,
        @Path("userId") userId: String
    )

    @PUT("reading-rooms/{id}/active-book")
    suspend fun changeReadingRoomBook(
        @Path("id") id: String,
        @Body body: ChangeReadingRoomBookRequestDto
    ): ReadingRoomDetailDto

    @POST("reading-rooms/{id}/ratings")
    suspend fun saveReadingRoomRating(
        @Path("id") id: String,
        @Body body: SaveReadingRoomRatingRequestDto
    ): ReadingRoomDetailDto

    @POST("reading-rooms/{id}/comments")
    suspend fun saveReadingRoomComment(
        @Path("id") id: String,
        @Body body: SaveReadingRoomCommentRequestDto
    ): ReadingRoomDetailDto

    @GET("library/me")
    suspend fun getOwnLibrary(): LibraryDto

    @GET("library/me/followed-books")
    suspend fun getFollowedBooks(): List<BookDto>

    @POST("library/me/books")
    suspend fun addBookToLibrary(@Body body: Map<String, String>)

    @HTTP(method = "DELETE", path = "library/me/books", hasBody = true)
    suspend fun removeBookFromLibrary(@Body body: Map<String, String>)

    @GET("profile/me")
    suspend fun getOwnProfile(): ProfileDto

    @GET("profile/me/preview")
    suspend fun getOwnPublicProfilePreview(): ProfileDto

    @GET("profile/public")
    suspend fun getPublicProfile(): ProfileDto

    @GET("stats")
    suspend fun getStats(): StatsDto

    @GET("notifications")
    suspend fun getNotifications(): NotificationsDto

    @PUT("profile/me")
    suspend fun updateOwnProfile(@Body body: UpdateProfileRequestDto): ProfileDto

    @POST("push/register")
    suspend fun registerPushToken(@Body body: RegisterPushTokenRequestDto)
}

data class RegisterPushTokenRequestDto(
    val token: String,
    val platform: String = "android"
)
