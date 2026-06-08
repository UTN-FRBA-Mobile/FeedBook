package com.example.feedbook.features.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.authors.domain.usecase.GetAuthorsUseCase
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val observeOwnLibraryUseCase: ObserveOwnLibraryUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(emptyLibraryUiState().copy(isLoading = true))
    val state: StateFlow<LibraryUiState> = _state.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadLibrary()
    }

    fun retry() {
        loadLibrary()
    }

    private fun loadLibrary() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            val followedAuthors = try {
                getAuthorsUseCase()
                    .filter { it.isFollowing }
                    .map { FollowedAuthorUiModel(id = it.id, name = it.name, imageUrl = it.imageUrl) }
            } catch (_: Exception) {
                emptyList()
            }

            observeOwnLibraryUseCase()
                .catch { throwable ->
                    _state.value = emptyLibraryUiState().copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load library.",
                        followedAuthors = followedAuthors
                    )
                }
                .collectLatest { library ->
                    _state.value = library.toUiState().copy(followedAuthors = followedAuthors)
                }
        }
    }

    companion object {
        fun provideFactory(
            observeOwnLibraryUseCase: ObserveOwnLibraryUseCase,
            getAuthorsUseCase: GetAuthorsUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel(observeOwnLibraryUseCase, getAuthorsUseCase) as T
            }
        }
    }
}
