package com.example.feedbook.core.network

import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.home.data.remote.dto.HomeDto
import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface ApiService {
    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDto

    @GET("explore/users")
    suspend fun getExploreUsers(): List<ExploreUserDto>

    @GET("books/{bookId}/progress")
    suspend fun getReadingProgress(
        @Path("bookId") bookId: String
    ): ReadingProgressDto?

    @GET("books/{bookId}/reviews")
    suspend fun getReviews(
        @Path("bookId") bookId: String
    ): List<ReviewDto>

    @GET("authors")
    suspend fun getAuthors(): List<AuthorDto>

    @GET("authors/{id}")
    suspend fun getAuthorById(@Path("id") id: String): AuthorDto

    @POST("authors/{id}/follow-toggle")
    suspend fun toggleFollow(@Path("id") id: String)

    @GET("home")
    suspend fun getHomeFeed(): HomeDto

    @GET("library/me")
    suspend fun getOwnLibrary(): LibraryDto

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
}
