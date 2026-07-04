package com.example.feedbook.features.books.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.authors.domain.usecase.GetAuthorsUseCase
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import com.example.feedbook.features.books.domain.usecase.GetExploreUsersUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookListViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getExploreUsersUseCase: GetExploreUsersUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookListState(isLoading = true))
    val state: StateFlow<BookListState> = _state.asStateFlow()
    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        preset = null,
        imageUri = null
    )

    init {
        loadBooks()
        viewModelScope.launch {
            observeOwnProfileUseCase()
                .catch { }
                .collectLatest { profile ->
                    avatarPresentation = profile.toAvatarPresentation()
                    _state.value = _state.value.withAvatar()
                }
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            runCatching {
                val books = getBooksUseCase()
                val authors = getAuthorsUseCase()
                val users = getExploreUsersUseCase()
                BookListState(books = books, authors = authors, users = users).withAvatar()
            }
                .onSuccess { state ->
                    _state.value = state
                }
                .onFailure { throwable ->
                    _state.value = BookListState(
                        error = throwable.message ?: "Unable to load books."
                    ).withAvatar()
                }
        }
    }

    private fun BookListState.withAvatar(): BookListState = copy(
        avatarStyle = avatarPresentation.style,
        avatarPreset = avatarPresentation.preset,
        avatarImageUri = avatarPresentation.imageUri
    )

    companion object {
        fun provideFactory(
            getBooksUseCase: GetBooksUseCase,
            getAuthorsUseCase: GetAuthorsUseCase,
            getExploreUsersUseCase: GetExploreUsersUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookListViewModel(
                    getBooksUseCase = getBooksUseCase,
                    getAuthorsUseCase = getAuthorsUseCase,
                    getExploreUsersUseCase = getExploreUsersUseCase,
                    observeOwnProfileUseCase = observeOwnProfileUseCase
                ) as T
            }
        }
    }
}
