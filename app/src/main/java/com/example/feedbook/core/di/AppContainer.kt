package com.example.feedbook.core.di

import android.content.Context
import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.features.authors.data.remote.AuthorRemoteDataSource
import com.example.feedbook.features.authors.data.repository.AuthorRepositoryImpl
import com.example.feedbook.features.authors.domain.usecase.GetAuthorsUseCase
import com.example.feedbook.features.authors.domain.usecase.GetAuthorByIdUseCase
import com.example.feedbook.features.authors.domain.usecase.ToggleAuthorFollowUseCase
import com.example.feedbook.core.session.SessionManager
import com.example.feedbook.core.session.SessionStorage
import com.example.feedbook.features.auth.data.remote.AuthRemoteDataSource
import com.example.feedbook.features.auth.data.repository.AuthRepositoryImpl
import com.example.feedbook.features.auth.domain.usecase.LoginUseCase
import com.example.feedbook.features.books.data.repository.BookRepositoryImpl
import com.example.feedbook.features.books.data.remote.BookRemoteDataSource
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import com.example.feedbook.features.books.domain.usecase.GetExploreUsersUseCase
import com.example.feedbook.features.books.domain.usecase.GetReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.SaveReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import com.example.feedbook.features.books.domain.usecase.SaveReviewUseCase
import com.example.feedbook.features.books.domain.usecase.ToggleLikeUseCase
import com.example.feedbook.features.home.data.remote.HomeRemoteDataSource
import com.example.feedbook.features.home.data.repository.HomeRepositoryImpl
import com.example.feedbook.features.home.domain.usecase.ObserveHomeFeedUseCase
import com.example.feedbook.features.library.data.remote.LibraryRemoteDataSource
import com.example.feedbook.features.library.data.repository.LibraryRepositoryImpl
import com.example.feedbook.features.library.domain.usecase.AddBookToLibraryUseCase
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
import com.example.feedbook.features.library.domain.usecase.RemoveBookFromLibraryUseCase
import com.example.feedbook.features.notifications.data.remote.NotificationsRemoteDataSource
import com.example.feedbook.features.notifications.data.repository.NotificationsRepositoryImpl
import com.example.feedbook.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.feedbook.features.profile.data.remote.ProfileRemoteDataSource
import com.example.feedbook.features.profile.data.repository.ProfileRepositoryImpl
import com.example.feedbook.features.profile.domain.usecase.GetPublicProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnPublicProfilePreviewUseCase
import com.example.feedbook.features.profile.domain.usecase.UpdateProfileUseCase
import com.example.feedbook.features.stats.data.remote.StatsRemoteDataSource
import com.example.feedbook.features.stats.data.repository.StatsRepositoryImpl
import com.example.feedbook.features.stats.domain.usecase.GetStatsUseCase

class AppContainer(
    context: Context
) {
    private val apiService = NetworkModule.apiService
    private val authApiService = NetworkModule.authApiService
    private val sessionStorage = SessionStorage(context)
    val sessionManager = SessionManager(sessionStorage)

    private val bookRemoteDataSource = BookRemoteDataSource(apiService)

    private val authorRemoteDataSource = AuthorRemoteDataSource(apiService)
    private val authRemoteDataSource = AuthRemoteDataSource(authApiService)
    private val homeRemoteDataSource = HomeRemoteDataSource(apiService)
    private val profileRemoteDataSource = ProfileRemoteDataSource(apiService)
    private val libraryRemoteDataSource = LibraryRemoteDataSource(apiService)
    private val statsRemoteDataSource = StatsRemoteDataSource(apiService)
    private val notificationsRemoteDataSource = NotificationsRemoteDataSource(apiService)

    private val authRepository = AuthRepositoryImpl(authRemoteDataSource)
    private val bookRepository = BookRepositoryImpl(bookRemoteDataSource)

    private val authorRepository = AuthorRepositoryImpl(authorRemoteDataSource)
    private val homeRepository = HomeRepositoryImpl(homeRemoteDataSource)
    private val profileRepository = ProfileRepositoryImpl(profileRemoteDataSource)
    private val libraryRepository = LibraryRepositoryImpl(libraryRemoteDataSource)
    private val statsRepository = StatsRepositoryImpl(statsRemoteDataSource)
    private val notificationsRepository = NotificationsRepositoryImpl(notificationsRemoteDataSource)

    val loginUseCase = LoginUseCase(authRepository)
    val getBooksUseCase = GetBooksUseCase(bookRepository)
    val getExploreUsersUseCase = GetExploreUsersUseCase(bookRepository)
    val getBookByIdUseCase = GetBookByIdUseCase(bookRepository)

    val getAuthorsUseCase = GetAuthorsUseCase(authorRepository)

    val toggleAuthorFollowUseCase = ToggleAuthorFollowUseCase(authorRepository)

    val getAuthorByIdUseCase = GetAuthorByIdUseCase(authorRepository)

    val getReadingProgress = GetReadingProgressUseCase(bookRepository)
    val saveReadingProgressUseCase = SaveReadingProgressUseCase(bookRepository)

    val getReviewsUseCase = GetReviewsUseCase(bookRepository)
    val saveReviewUseCase = SaveReviewUseCase(bookRepository)
    val toggleLikeUseCase = ToggleLikeUseCase(bookRepository)

    val observeHomeFeedUseCase = ObserveHomeFeedUseCase(homeRepository)
    val observeOwnProfileUseCase = ObserveOwnProfileUseCase(profileRepository)
    val observeOwnPublicProfilePreviewUseCase =
        ObserveOwnPublicProfilePreviewUseCase(profileRepository)
    val getPublicProfileUseCase = GetPublicProfileUseCase(profileRepository)
    val updateProfileUseCase = UpdateProfileUseCase(profileRepository)
    val observeOwnLibraryUseCase = ObserveOwnLibraryUseCase(libraryRepository)
    val addBookToLibraryUseCase = AddBookToLibraryUseCase(libraryRepository)
    val removeBookFromLibraryUseCase = RemoveBookFromLibraryUseCase(libraryRepository)
    val getStatsUseCase = GetStatsUseCase(statsRepository)
    val getNotificationsUseCase = GetNotificationsUseCase(notificationsRepository)
}
