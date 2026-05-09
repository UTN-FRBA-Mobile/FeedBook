package com.example.feedbook.core.di

import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.features.books.data.repository.BookRepositoryImpl
import com.example.feedbook.features.books.data.remote.BookRemoteDataSource
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import com.example.feedbook.features.home.data.remote.HomeRemoteDataSource
import com.example.feedbook.features.home.data.repository.HomeRepositoryImpl
import com.example.feedbook.features.home.domain.usecase.ObserveHomeFeedUseCase
import com.example.feedbook.features.library.data.remote.LibraryRemoteDataSource
import com.example.feedbook.features.library.data.repository.LibraryRepositoryImpl
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
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
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend

class AppContainer {
    private val apiService = NetworkModule.apiService
    private val fakeBackend = FakeFeedBookBackend()

    private val bookRemoteDataSource = BookRemoteDataSource(apiService)
    private val homeRemoteDataSource = HomeRemoteDataSource(fakeBackend)
    private val profileRemoteDataSource = ProfileRemoteDataSource(fakeBackend)
    private val libraryRemoteDataSource = LibraryRemoteDataSource(fakeBackend)
    private val statsRemoteDataSource = StatsRemoteDataSource(fakeBackend)
    private val notificationsRemoteDataSource = NotificationsRemoteDataSource(fakeBackend)

    private val bookRepository = BookRepositoryImpl(bookRemoteDataSource)
    private val homeRepository = HomeRepositoryImpl(homeRemoteDataSource)
    private val profileRepository = ProfileRepositoryImpl(profileRemoteDataSource)
    private val libraryRepository = LibraryRepositoryImpl(libraryRemoteDataSource)
    private val statsRepository = StatsRepositoryImpl(statsRemoteDataSource)
    private val notificationsRepository = NotificationsRepositoryImpl(notificationsRemoteDataSource)

    val getBooksUseCase = GetBooksUseCase(bookRepository)
    val getBookByIdUseCase = GetBookByIdUseCase(bookRepository)
    val observeHomeFeedUseCase = ObserveHomeFeedUseCase(homeRepository)
    val observeOwnProfileUseCase = ObserveOwnProfileUseCase(profileRepository)
    val observeOwnPublicProfilePreviewUseCase =
        ObserveOwnPublicProfilePreviewUseCase(profileRepository)
    val getPublicProfileUseCase = GetPublicProfileUseCase(profileRepository)
    val updateProfileUseCase = UpdateProfileUseCase(profileRepository)
    val observeOwnLibraryUseCase = ObserveOwnLibraryUseCase(libraryRepository)
    val getStatsUseCase = GetStatsUseCase(statsRepository)
    val getNotificationsUseCase = GetNotificationsUseCase(notificationsRepository)
}
