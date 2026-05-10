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
    private val _state = MutableStateFlow(
        emptyProfileUiState(ProfileVariant.PUBLIC).copy(isLoading = true)
    )
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun retry() {
        loadProfile()
    }

    private fun loadProfile() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { getPublicProfileUseCase() }
                .onSuccess { _state.value = it.toPublicProfileUiState() }
                .onFailure { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
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
