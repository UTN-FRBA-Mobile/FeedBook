package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.usecase.GetPublicProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.ToggleUserFollowUseCase
import com.example.feedbook.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PublicProfileViewModel(
    private val userId: String,
    private val getPublicProfileUseCase: GetPublicProfileUseCase,
    private val toggleUserFollowUseCase: ToggleUserFollowUseCase
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

    fun toggleFollow() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.value = currentState.copy(
                isFollowing = !currentState.isFollowing,
                actionLabelRes = if (!currentState.isFollowing)
                    R.string.profile_action_following else R.string.profile_action_follow
            )
            runCatching { toggleUserFollowUseCase(userId) }
                .onFailure { _state.value = currentState }
        }
    }

    private fun loadProfile() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { getPublicProfileUseCase(userId) }
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
            userId: String,
            getPublicProfileUseCase: GetPublicProfileUseCase,
            toggleUserFollowUseCase: ToggleUserFollowUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PublicProfileViewModel(
                    userId,
                    getPublicProfileUseCase,
                    toggleUserFollowUseCase
                ) as T
            }
        }
    }
}
