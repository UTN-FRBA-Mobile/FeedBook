package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnPublicProfilePreviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PublicProfilePreviewViewModel(
    observeOwnPublicProfilePreviewUseCase: ObserveOwnPublicProfilePreviewUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(samplePublicProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeOwnPublicProfilePreviewUseCase().collectLatest { profile ->
                _state.value = profile.toPublicProfileUiState()
            }
        }
    }

    companion object {
        fun provideFactory(
            observeOwnPublicProfilePreviewUseCase: ObserveOwnPublicProfilePreviewUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PublicProfilePreviewViewModel(observeOwnPublicProfilePreviewUseCase) as T
            }
        }
    }
}
