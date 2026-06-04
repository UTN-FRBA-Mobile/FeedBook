package com.example.feedbook.features.books.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import com.example.feedbook.features.books.domain.usecase.GetReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import com.example.feedbook.features.books.domain.usecase.SaveReadingProgressUseCase
import com.example.feedbook.features.books.domain.usecase.SaveReviewUseCase
import com.example.feedbook.features.library.domain.usecase.AddBookToLibraryUseCase
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
import com.example.feedbook.features.library.domain.usecase.RemoveBookFromLibraryUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookId: String,
    private val getBookByIdUseCase: GetBookByIdUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReadingProgressUseCase: GetReadingProgressUseCase,
    private val saveReadingProgressUseCase: SaveReadingProgressUseCase,
    private val saveReviewUseCase: SaveReviewUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val observeOwnLibraryUseCase: ObserveOwnLibraryUseCase,
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
        observeLibrary()
        viewModelScope.launch {
            observeOwnProfileUseCase()
                .catch { }
                .collectLatest { profile ->
                    avatarPresentation = profile.toAvatarPresentation()
                    _state.value = _state.value.copy(
                        avatarStyle = avatarPresentation.style,
                        avatarPreset = avatarPresentation.preset,
                        avatarImageUri = avatarPresentation.imageUri
                    )
                }
        }
    }

    fun toggleBookInLibrary() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isTogglingLibrary = true, libraryFeedback = null)
            val result = if (_state.value.isBookInLibrary) {
                runCatching { removeBookFromLibraryUseCase(bookId) }
            } else {
                runCatching { addBookToLibraryUseCase(bookId) }
            }
            result.onSuccess {
                val msg = if (_state.value.isBookInLibrary) "Book removed from your list" else "Book added to your list"
                _state.value = _state.value.copy(
                    isBookInLibrary = !_state.value.isBookInLibrary,
                    isTogglingLibrary = false,
                    libraryFeedback = msg
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    isTogglingLibrary = false,
                    libraryFeedback = "Something went wrong"
                )
            }
        }
    }

    fun clearLibraryFeedback() {
        _state.value = _state.value.copy(libraryFeedback = null)
    }

    fun saveProgress(currentPage: Int) {
        viewModelScope.launch {
            runCatching { saveReadingProgressUseCase(bookId, currentPage) }
                .onSuccess { progress ->
                    _state.value = _state.value.copy(
                        readingProgress = progress.toUiModel()
                    )
                }
        }
    }

    private fun observeLibrary() {
        viewModelScope.launch {
            observeOwnLibraryUseCase()
                .catch { }
                .collectLatest { library ->
                    val isInLibrary = library.currentBook.id == bookId ||
                        library.readingBooks.any { it.id == bookId } ||
                        library.shelfBooks.any { it.id == bookId }
                    _state.value = _state.value.copy(isBookInLibrary = isInLibrary)
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

            val reviews = reviewsResult.getOrDefault(emptyList()).map { it.toUiModel() }
            val userReview = reviews.find { it.userId == "me" }

            _state.value = BookDetailUiState(
                isLoading = false,
                avatarStyle = avatarPresentation.style,
                avatarPreset = avatarPresentation.preset,
                avatarImageUri = avatarPresentation.imageUri,
                book = bookResult.getOrNull()?.toUiModel(),
                reviews = reviews,
                userReview = userReview,
                readingProgress = progressResult.getOrNull()?.toUiModel(),
                error = bookResult.exceptionOrNull()?.message
                    ?: if (bookResult.isSuccess && bookResult.getOrNull() == null) "Libro no encontrado" else null
            )
        }
    }

    fun saveReview(rating: Float, text: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSavingReview = true, reviewFeedback = null)
            runCatching { saveReviewUseCase(bookId, rating, text) }
                .onSuccess {
                    val msg = if (_state.value.userReview != null) "Review updated" else "Review posted"
                    _state.value = _state.value.copy(
                        isSavingReview = false,
                        reviewFeedback = msg
                    )
                    loadBook()
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isSavingReview = false,
                        reviewFeedback = "Something went wrong"
                    )
                }
        }
    }

    fun clearReviewFeedback() {
        _state.value = _state.value.copy(reviewFeedback = null)
    }

    companion object {
        fun provideFactory(
            bookId: String,
            getBookByIdUseCase: GetBookByIdUseCase,
            getReviewsUseCase: GetReviewsUseCase,
            getReadingProgressUseCase: GetReadingProgressUseCase,
            saveReadingProgressUseCase: SaveReadingProgressUseCase,
            saveReviewUseCase: SaveReviewUseCase,
            addBookToLibraryUseCase: AddBookToLibraryUseCase,
            removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
            observeOwnLibraryUseCase: ObserveOwnLibraryUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BookDetailViewModel(
                    bookId,
                    getBookByIdUseCase,
                    getReviewsUseCase,
                    getReadingProgressUseCase,
                    saveReadingProgressUseCase,
                    saveReviewUseCase,
                    addBookToLibraryUseCase,
                    removeBookFromLibraryUseCase,
                    observeOwnLibraryUseCase,
                    observeOwnProfileUseCase
                ) as T
        }
    }
}
