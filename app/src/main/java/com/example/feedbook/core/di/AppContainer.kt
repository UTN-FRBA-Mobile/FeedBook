package com.example.feedbook.core.di

import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.data.remote.BookRemoteDataSource
import com.example.feedbook.data.repository.BookRepositoryImpl
import com.example.feedbook.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.domain.usecase.GetBooksUseCase

class AppContainer {
    private val apiService = NetworkModule.apiService
    private val remoteDataSource = BookRemoteDataSource(apiService)
    private val bookRepository = BookRepositoryImpl(remoteDataSource)

    val getBooksUseCase = GetBooksUseCase(bookRepository)
    val getBookByIdUseCase = GetBookByIdUseCase(bookRepository)
}
