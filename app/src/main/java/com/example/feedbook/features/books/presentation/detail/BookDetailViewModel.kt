package com.example.feedbook.features.books.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookId: String,
    private val getBookByIdUseCase: GetBookByIdUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReadingProgressUseCase: GetReadingProgressUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState(isLoading = true))
    val state: StateFlow<BookDetailState> = _state.asStateFlow()

    init {
        loadBook()
    }

    // BookDetailViewModel.kt
    fun loadBook() {
        viewModelScope.launch {
            _state.value = BookDetailState(isLoading = true)

            val bookDeferred      = async { runCatching { getBookByIdUseCase(bookId) } }
            val reviewsDeferred   = async { runCatching { getReviewsUseCase(bookId) } }
            val progressDeferred  = async { runCatching { getReadingProgressUseCase(bookId) } }

            val book      = bookDeferred.await()
            val reviews   = reviewsDeferred.await()
            val progress  = progressDeferred.await()

            _state.value = BookDetailState(
                book = book.getOrNull(),
                reviews = reviews.getOrElse { emptyList() },
                readingProgress = progress.getOrNull(),
                error = if (book.isFailure) book.exceptionOrNull()?.message else null
            )
        }
    }

    companion object {
        fun provideFactory(
            bookId: String,
            getBookByIdUseCase: GetBookByIdUseCase,
            getReviewsUseCase: GetReviewsUseCase,
            getReadingProgressUseCase: GetReadingProgressUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookDetailViewModel(bookId, getBookByIdUseCase, getReviewsUseCase, getReadingProgressUseCase) as T
            }
        }
    }
}
