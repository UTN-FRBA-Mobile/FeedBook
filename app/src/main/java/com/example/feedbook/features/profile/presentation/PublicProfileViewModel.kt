package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.usecase.GetPublicProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PublicProfileViewModel(
    private val getPublicProfileUseCase: GetPublicProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(samplePublicProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { getPublicProfileUseCase() }
                .onSuccess { _state.value = it.toPublicProfileUiState() }
        }
    }

    companion object {
        fun provideFactory(
            getPublicProfileUseCase: GetPublicProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PublicProfileViewModel(getPublicProfileUseCase) as T
            }
        }
    }
}
