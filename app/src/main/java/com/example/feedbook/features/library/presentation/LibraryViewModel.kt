package com.example.feedbook.features.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val observeOwnLibraryUseCase: ObserveOwnLibraryUseCase
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
            observeOwnLibraryUseCase()
                .catch { throwable ->
                    _state.value = emptyLibraryUiState().copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load library."
                    )
                }
                .collectLatest { library ->
                    _state.value = library.toUiState()
                }
        }
    }

    companion object {
        fun provideFactory(
            observeOwnLibraryUseCase: ObserveOwnLibraryUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel(observeOwnLibraryUseCase) as T
            }
        }
    }
}
