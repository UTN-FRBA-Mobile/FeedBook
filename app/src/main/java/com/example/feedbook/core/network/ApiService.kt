package com.example.feedbook.core.network

import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ReadingProgressDto
import com.example.feedbook.features.books.data.remote.dto.ReviewDto
import com.example.feedbook.features.books.domain.model.ReadingProgress
import com.example.feedbook.features.notifications.data.remote.dto.NotificationsDto
import com.example.feedbook.features.profile.data.remote.dto.ProfileDto
import com.example.feedbook.features.stats.data.remote.dto.StatsDto
import com.example.feedbook.features.profile.data.remote.dto.UpdateProfileRequestDto
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: String): BookDto

    @GET("books/{bookId}/progress")
    suspend fun getReadingProgress(
        @Path("bookId") bookId: String
    ): ReadingProgressDto?

    @GET("books/{bookId}/reviews")
    suspend fun getReviews(
        @Path("bookId") bookId: String
    ): List<ReviewDto>

    @GET("profile/me")
    suspend fun getOwnProfile(): ProfileDto

    @GET("profile/public")
    suspend fun getPublicProfile(): ProfileDto

    @GET("stats")
    suspend fun getStats(): StatsDto

    @GET("notifications")
    suspend fun getNotifications(): NotificationsDto

    @PUT("profile/me")
    suspend fun updateOwnProfile(@Body body: UpdateProfileRequestDto): ProfileDto
}
