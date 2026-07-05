package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetUserFollowersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserFollowersUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val followers: List<com.example.feedbook.features.books.domain.model.ExploreUser> = emptyList()
)

class UserFollowersViewModel(
    private val userId: String,
    private val getUserFollowersUseCase: GetUserFollowersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserFollowersUiState(isLoading = true))
    val state: StateFlow<UserFollowersUiState> = _state.asStateFlow()

    init {
        loadFollowers()
    }

    fun retry() {
        loadFollowers()
    }

    private fun loadFollowers() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { getUserFollowersUseCase(userId) }
                .onSuccess { followers ->
                    _state.value = UserFollowersUiState(
                        isLoading = false,
                        followers = followers
                    )
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
            getUserFollowersUseCase: GetUserFollowersUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserFollowersViewModel(userId, getUserFollowersUseCase) as T
            }
        }
    }
}
