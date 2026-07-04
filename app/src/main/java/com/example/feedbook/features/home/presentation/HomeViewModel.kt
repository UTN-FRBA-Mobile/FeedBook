package com.example.feedbook.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.home.domain.usecase.ObserveHomeFeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(
    private val observeHomeFeedUseCase: ObserveHomeFeedUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(emptyHomeUiState().copy(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadHome()
    }

    fun retry() {
        loadHome()
    }

    private fun loadHome() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            observeHomeFeedUseCase()
                .catch { throwable ->
                    _state.value = emptyHomeUiState().copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load home."
                    )
                }
                .collectLatest { feed ->
                    _state.value = feed.toUiState()
                }
        }
    }

    companion object {
        fun provideFactory(
            observeHomeFeedUseCase: ObserveHomeFeedUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(observeHomeFeedUseCase) as T
            }
        }
    }
}
