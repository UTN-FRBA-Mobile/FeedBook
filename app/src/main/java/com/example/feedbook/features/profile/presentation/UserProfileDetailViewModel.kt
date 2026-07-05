package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetExploreUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileDetailViewModel(
    private val userId: String,
    private val getExploreUsersUseCase: GetExploreUsersUseCase
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
            runCatching { getExploreUsersUseCase() }
                .onSuccess { users ->
                    val user = users.firstOrNull { it.id == userId }
                    _state.value = if (user != null) {
                        user.toProfileUiState().copy(isLoading = false)
                    } else {
                        _state.value.copy(
                            isLoading = false,
                            errorMessage = "Usuario no encontrado"
                        )
                    }
                }
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
            userId: String,
            getExploreUsersUseCase: GetExploreUsersUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserProfileDetailViewModel(userId, getExploreUsersUseCase) as T
            }
        }
    }
}
