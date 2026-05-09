package com.example.feedbook.features.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.library.domain.usecase.ObserveOwnLibraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LibraryViewModel(
    observeOwnLibraryUseCase: ObserveOwnLibraryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(sampleLibraryUiState())
    val state: StateFlow<LibraryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeOwnLibraryUseCase().collectLatest { library ->
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
