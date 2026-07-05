package com.example.feedbook.features.books.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.authors.domain.usecase.GetAuthorsUseCase
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import com.example.feedbook.features.books.domain.usecase.GetExploreUsersUseCase
import com.example.feedbook.features.books.domain.usecase.SearchExploreUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class BookListViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getExploreUsersUseCase: GetExploreUsersUseCase,
    private val searchExploreUseCase: SearchExploreUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookListState(isLoading = true))
    val state: StateFlow<BookListState> = _state.asStateFlow()
    private var searchJob: Job? = null
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
        runSearch(debounceMillis = 0, forceLoading = true)
    }

    fun updateSearch(
        query: String = _state.value.query,
        selectedGenres: Set<String> = _state.value.selectedGenres,
        selectedAuthors: Set<String> = _state.value.selectedAuthors
    ) {
        _state.value = _state.value.copy(
            query = query,
            selectedGenres = selectedGenres,
            selectedAuthors = selectedAuthors
        )
        runSearch(debounceMillis = SEARCH_DEBOUNCE_MILLIS, forceLoading = false)
    }

    private fun runSearch(debounceMillis: Long, forceLoading: Boolean) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (debounceMillis > 0) {
                delay(debounceMillis)
            }
            val currentState = _state.value
            val query = currentState.query
            val selectedGenres = currentState.selectedGenres
            val selectedAuthors = currentState.selectedAuthors
            val hasExistingContent = currentState.books.isNotEmpty() ||
                currentState.authors.isNotEmpty() ||
                currentState.users.isNotEmpty()

            _state.value = currentState.copy(
                isLoading = forceLoading && !hasExistingContent,
                isRefreshing = !forceLoading || hasExistingContent,
                error = null
            )

            runCatching {
                if (query.trim().isBlank()) {
                    supervisorScope {
                        val booksDeferred = async { getBooksUseCase() }
                        val authorsDeferred = async { getAuthorsUseCase() }
                        val usersDeferred = async { getExploreUsersUseCase() }
                        Triple(
                            booksDeferred.await(),
                            authorsDeferred.await(),
                            usersDeferred.await()
                        )
                    }
                } else {
                    val results = searchExploreUseCase(query)
                    Triple(results.books, results.authors, results.users)
                }
            }
                .onSuccess { (books, authors, users) ->
                    val visibleBooks = books.filter { book ->
                        selectedGenres.isEmpty() || selectedGenres.contains(book.genre)
                    }.filter { book ->
                        selectedAuthors.isEmpty() || selectedAuthors.contains(book.author)
                    }
                    val visibleAuthors = if (selectedAuthors.isEmpty()) {
                        authors
                    } else {
                        authors.filter { author -> selectedAuthors.contains(author.name) }
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        books = visibleBooks,
                        authors = visibleAuthors,
                        users = users,
                        error = null
                    ).withAvatar()
                }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = throwable.message ?: "Unable to load explore results."
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
        private const val SEARCH_DEBOUNCE_MILLIS = 300L

        fun provideFactory(
            getBooksUseCase: GetBooksUseCase,
            getAuthorsUseCase: GetAuthorsUseCase,
            getExploreUsersUseCase: GetExploreUsersUseCase,
            searchExploreUseCase: SearchExploreUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookListViewModel(
                    getBooksUseCase = getBooksUseCase,
                    getAuthorsUseCase = getAuthorsUseCase,
                    getExploreUsersUseCase = getExploreUsersUseCase,
                    searchExploreUseCase = searchExploreUseCase,
                    observeOwnProfileUseCase = observeOwnProfileUseCase
                ) as T
            }
        }
    }
}
