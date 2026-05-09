package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnPublicProfilePreviewUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PublicProfilePreviewViewModel(
    private val observeOwnPublicProfilePreviewUseCase: ObserveOwnPublicProfilePreviewUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        emptyProfileUiState(ProfileVariant.PUBLIC).copy(isLoading = true)
    )
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPreview()
    }

    fun retry() {
        loadPreview()
    }

    private fun loadPreview() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            observeOwnPublicProfilePreviewUseCase()
                .catch { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collectLatest { profile ->
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
