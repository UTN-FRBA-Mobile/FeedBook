package com.example.feedbook.features.books.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import kotlinx.coroutines.flow.collectLatest

class BookDetailViewModel(
    private val bookId: String,
    private val getBookByIdUseCase: GetBookByIdUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReadingProgressUseCase: GetReadingProgressUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailUiState(isLoading = true))
    val state: StateFlow<BookDetailUiState> = _state.asStateFlow()

    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        preset = null,
        imageUri = null
    )

    init {
        loadBook()
        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                avatarPresentation = profile.toAvatarPresentation()
                _state.value = _state.value.copy(
                    avatarStyle = avatarPresentation.style,
                    avatarPreset = avatarPresentation.preset,
                    avatarImageUri = avatarPresentation.imageUri
                )
            }
        }
    }

    fun loadBook() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val bookDeferred = async { runCatching { getBookByIdUseCase(bookId) } }
            val reviewsDeferred = async { runCatching { getReviewsUseCase(bookId) } }
            val progressDeferred = async { runCatching { getReadingProgressUseCase(bookId) } }

            val bookResult = bookDeferred.await()
            val reviewsResult = reviewsDeferred.await()
            val progressResult = progressDeferred.await()

            _state.value = BookDetailUiState(
                isLoading = false,
                avatarStyle = avatarPresentation.style,
                avatarPreset = avatarPresentation.preset,
                avatarImageUri = avatarPresentation.imageUri,

                book = bookResult.getOrNull()?.toUiModel(),

                reviews = reviewsResult.getOrDefault(emptyList()).map { it.toUiModel() },

                readingProgress = progressResult.getOrNull()?.toUiModel(),

                error = bookResult.exceptionOrNull()?.message
                    ?: if (bookResult.isSuccess && bookResult.getOrNull() == null) "Libro no encontrado" else null
            )
        }
    }

    companion object {
        fun provideFactory(
            bookId: String,
            getBookByIdUseCase: GetBookByIdUseCase,
            getReviewsUseCase: GetReviewsUseCase,
            getReadingProgressUseCase: GetReadingProgressUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BookDetailViewModel(
                    bookId,
                    getBookByIdUseCase,
                    getReviewsUseCase,
                    getReadingProgressUseCase,
                    observeOwnProfileUseCase
                ) as T
        }
    }
}
