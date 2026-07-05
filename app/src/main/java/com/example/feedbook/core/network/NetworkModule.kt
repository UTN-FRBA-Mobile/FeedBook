package com.example.feedbook.core.network

import android.content.Context
import com.example.feedbook.BuildConfig
import com.example.feedbook.core.session.SessionStorage
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
import com.example.feedbook.features.auth.data.remote.dto.LoginRequestDto
import com.example.feedbook.features.auth.data.remote.dto.LoginResponseDto
import com.example.feedbook.features.auth.data.remote.dto.RegisterRequestDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    @Volatile
    private var wifiNetworkSelector: WifiNetworkSelector? = null

    @Volatile
    private var authToken: String? = null

    @Volatile
    private var backendOrigin = BackendUrls.origin(BuildConfig.BACKEND_ORIGIN)

    @Volatile
    private var currentServices = createServices(backendOrigin)

    fun initialize(context: Context) {
        wifiNetworkSelector = WifiNetworkSelector(context)
        authToken = SessionStorage(context).readToken()
        updateBackendOrigin(BackendServerConfig(context).getOrigin())
    }

    fun updateAuthToken(token: String?) {
        authToken = token
    }

    fun updateBackendOrigin(rawOrigin: String): String {
        val normalizedOrigin = BackendUrls.origin(rawOrigin)
        backendOrigin = normalizedOrigin
        currentServices = createServices(normalizedOrigin)
        return normalizedOrigin
    }

    fun currentBackendOrigin(): String = backendOrigin

    val apiService: ApiService = DelegatingApiService()

    val authApiService: AuthApiService = DelegatingAuthApiService()

    private fun createServices(origin: String): NetworkServices {
        val localSocketFactory = if (BackendUrls.shouldBindToWifi(origin)) {
            wifiNetworkSelector?.socketFactoryForWifi()
        } else {
            null
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BackendUrls.apiBaseUrl(origin))
            .client(
                ApiClient.createOkHttpClient(
                    socketFactory = localSocketFactory,
                    authTokenProvider = { authToken }
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val authRetrofit = Retrofit.Builder()
            .baseUrl(origin)
            .client(ApiClient.createOkHttpClient(useSystemProxy = false, socketFactory = localSocketFactory))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return NetworkServices(
            apiService = retrofit.create(ApiService::class.java),
            authApiService = authRetrofit.create(AuthApiService::class.java)
        )
    }

    private data class NetworkServices(
        val apiService: ApiService,
        val authApiService: AuthApiService
    )

    private class DelegatingAuthApiService : AuthApiService {
        override suspend fun login(body: LoginRequestDto): LoginResponseDto =
            currentServices.authApiService.login(body)

        override suspend fun register(body: RegisterRequestDto): LoginResponseDto =
            currentServices.authApiService.register(body)
    }

    private class DelegatingApiService : ApiService {
        override suspend fun getBooks(): List<BookDto> = currentServices.apiService.getBooks()

        override suspend fun getBookById(id: String): BookDto =
            currentServices.apiService.getBookById(id)

        override suspend fun getBookByIsbn(isbn: String): BookDto =
            currentServices.apiService.getBookByIsbn(isbn)

        override suspend fun getExploreUsers(): List<ExploreUserDto> =
            currentServices.apiService.getExploreUsers()

        override suspend fun getReadingProgress(bookId: String): ReadingProgressDto? =
            currentServices.apiService.getReadingProgress(bookId)

        override suspend fun updateReadingProgress(
            bookId: String,
            body: Map<String, Int>
        ): ReadingProgressDto = currentServices.apiService.updateReadingProgress(bookId, body)

        override suspend fun getReviews(
            bookId: String,
            page: Int,
            limit: Int
        ): ReviewsResponseDto = currentServices.apiService.getReviews(bookId, page, limit)

        override suspend fun saveReview(bookId: String, body: SaveReviewRequestDto): ReviewDto =
            currentServices.apiService.saveReview(bookId, body)

        override suspend fun toggleLike(bookId: String, reviewId: String): ReviewDto =
            currentServices.apiService.toggleLike(bookId, reviewId)

        override suspend fun getAuthors(): List<AuthorDto> =
            currentServices.apiService.getAuthors()

        override suspend fun getAuthorById(id: String): AuthorDto =
            currentServices.apiService.getAuthorById(id)

        override suspend fun toggleFollow(id: String) {
            currentServices.apiService.toggleFollow(id)
        }

        override suspend fun getHomeFeed(): HomeDto =
            currentServices.apiService.getHomeFeed()

        override suspend fun getReadingRooms(): ReadingRoomListDto =
            currentServices.apiService.getReadingRooms()

        override suspend fun createReadingRoom(body: CreateReadingRoomRequestDto): ReadingRoomDetailDto =
            currentServices.apiService.createReadingRoom(body)

        override suspend fun getReadingRoom(id: String): ReadingRoomDetailDto =
            currentServices.apiService.getReadingRoom(id)

        override suspend fun joinReadingRoom(id: String): ReadingRoomDetailDto =
            currentServices.apiService.joinReadingRoom(id)

        override suspend fun leaveReadingRoom(id: String) {
            currentServices.apiService.leaveReadingRoom(id)
        }

        override suspend fun updateReadingRoomDescription(
            id: String,
            body: UpdateReadingRoomDescriptionRequestDto
        ): ReadingRoomDetailDto = currentServices.apiService.updateReadingRoomDescription(id, body)

        override suspend fun deleteReadingRoom(id: String, body: DeleteReadingRoomRequestDto) {
            currentServices.apiService.deleteReadingRoom(id, body)
        }

        override suspend fun kickReadingRoomMember(id: String, userId: String) {
            currentServices.apiService.kickReadingRoomMember(id, userId)
        }

        override suspend fun changeReadingRoomBook(
            id: String,
            body: ChangeReadingRoomBookRequestDto
        ): ReadingRoomDetailDto = currentServices.apiService.changeReadingRoomBook(id, body)

        override suspend fun saveReadingRoomRating(
            id: String,
            body: SaveReadingRoomRatingRequestDto
        ): ReadingRoomDetailDto = currentServices.apiService.saveReadingRoomRating(id, body)

        override suspend fun saveReadingRoomComment(
            id: String,
            body: SaveReadingRoomCommentRequestDto
        ): ReadingRoomDetailDto = currentServices.apiService.saveReadingRoomComment(id, body)

        override suspend fun getOwnLibrary(): LibraryDto =
            currentServices.apiService.getOwnLibrary()

        override suspend fun getFollowedBooks(): List<BookDto> =
            currentServices.apiService.getFollowedBooks()

        override suspend fun addBookToLibrary(body: Map<String, String>) {
            currentServices.apiService.addBookToLibrary(body)
        }

        override suspend fun removeBookFromLibrary(body: Map<String, String>) {
            currentServices.apiService.removeBookFromLibrary(body)
        }

        override suspend fun getOwnProfile(): ProfileDto =
            currentServices.apiService.getOwnProfile()

        override suspend fun getOwnPublicProfilePreview(): ProfileDto =
            currentServices.apiService.getOwnPublicProfilePreview()

        override suspend fun getPublicProfile(): ProfileDto =
            currentServices.apiService.getPublicProfile()

        override suspend fun getStats(): StatsDto =
            currentServices.apiService.getStats()

        override suspend fun getNotifications(): NotificationsDto =
            currentServices.apiService.getNotifications()

        override suspend fun updateOwnProfile(body: UpdateProfileRequestDto): ProfileDto =
            currentServices.apiService.updateOwnProfile(body)

        override suspend fun uploadOwnAvatar(image: okhttp3.MultipartBody.Part) =
            currentServices.apiService.uploadOwnAvatar(image)

        override suspend fun registerPushToken(body: RegisterPushTokenRequestDto) {
            currentServices.apiService.registerPushToken(body)
        }

        override suspend fun unlinkPushToken(body: UnlinkPushTokenRequestDto) {
            currentServices.apiService.unlinkPushToken(body)
        }
    }
}
