package com.example.feedbook.core.di

import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.features.books.data.remote.BookRemoteDataSource
import com.example.feedbook.shared.fakebackend.FakeFeedBookBackend
import com.example.feedbook.features.notifications.data.remote.NotificationsRemoteDataSource
import com.example.feedbook.features.profile.data.remote.ProfileRemoteDataSource
import com.example.feedbook.features.stats.data.remote.StatsRemoteDataSource
import com.example.feedbook.features.books.data.repository.BookRepositoryImpl
import com.example.feedbook.features.notifications.data.repository.NotificationsRepositoryImpl
import com.example.feedbook.features.profile.data.repository.ProfileRepositoryImpl
import com.example.feedbook.features.stats.data.repository.StatsRepositoryImpl
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import com.example.feedbook.features.books.domain.usecase.GetReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import com.example.feedbook.features.notifications.domain.usecase.GetNotificationsUseCase
import com.example.feedbook.features.profile.domain.usecase.GetPublicProfileUseCase
import com.example.feedbook.features.stats.domain.usecase.GetStatsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnPublicProfilePreviewUseCase
import com.example.feedbook.features.profile.domain.usecase.UpdateProfileUseCase

class AppContainer {
    private val apiService = NetworkModule.apiService
    private val fakeBackend = FakeFeedBookBackend()

    private val bookRemoteDataSource = BookRemoteDataSource(apiService)
    private val profileRemoteDataSource = ProfileRemoteDataSource(fakeBackend)
    private val statsRemoteDataSource = StatsRemoteDataSource(fakeBackend)
    private val notificationsRemoteDataSource = NotificationsRemoteDataSource(fakeBackend)

    private val bookRepository = BookRepositoryImpl(bookRemoteDataSource)
    private val profileRepository = ProfileRepositoryImpl(profileRemoteDataSource)
    private val statsRepository = StatsRepositoryImpl(statsRemoteDataSource)
    private val notificationsRepository = NotificationsRepositoryImpl(notificationsRemoteDataSource)

    val getBooksUseCase = GetBooksUseCase(bookRepository)
    val getBookByIdUseCase = GetBookByIdUseCase(bookRepository)

    val getReadingProgress = GetReadingProgressUseCase(bookRepository)

    val getReviewsUseCase = GetReviewsUseCase(bookRepository)

    val observeOwnProfileUseCase = ObserveOwnProfileUseCase(profileRepository)
    val observeOwnPublicProfilePreviewUseCase =
        ObserveOwnPublicProfilePreviewUseCase(profileRepository)
    val getPublicProfileUseCase = GetPublicProfileUseCase(profileRepository)
    val updateProfileUseCase = UpdateProfileUseCase(profileRepository)
    val getStatsUseCase = GetStatsUseCase(statsRepository)
    val getNotificationsUseCase = GetNotificationsUseCase(notificationsRepository)
}
