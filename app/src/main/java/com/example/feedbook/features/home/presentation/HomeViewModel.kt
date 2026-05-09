package com.example.feedbook.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.home.domain.usecase.ObserveHomeFeedUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    observeHomeFeedUseCase: ObserveHomeFeedUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(sampleHomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeHomeFeedUseCase().collectLatest { feed ->
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
