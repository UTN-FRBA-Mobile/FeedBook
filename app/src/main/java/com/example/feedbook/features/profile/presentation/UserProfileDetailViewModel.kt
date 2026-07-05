package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetExploreUsersUseCase
import com.example.feedbook.features.profile.domain.usecase.ToggleUserFollowUseCase
import com.example.feedbook.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileDetailViewModel(
    private val userId: String,
    private val getExploreUsersUseCase: GetExploreUsersUseCase,
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
            runCatching { toggleUserFollowUseCase(userId) }
                .onSuccess { response ->
                    _state.value = currentState.copy(
                        isFollowing = response.isFollowing,
                        actionLabelRes = if (response.isFollowing) {
                            R.string.profile_action_following
                        } else {
                            R.string.profile_action_follow
                        },
                        profileStats = currentState.profileStats.map { stat ->
                            if (stat.label.equals("Followers", ignoreCase = true)) {
                                stat.copy(value = formatFollowerCount(response.followerCount))
                            } else {
                                stat
                            }
                        }
                    )
                }
                .onFailure { _state.value = currentState }
        }
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

    private fun formatFollowerCount(count: Int): String {
        return if (count >= 1000) {
            String.format("%.1fK followers", count / 1000f)
        } else {
            "$count followers"
        }
    }

    companion object {
        fun provideFactory(
            userId: String,
            getExploreUsersUseCase: GetExploreUsersUseCase,
            toggleUserFollowUseCase: ToggleUserFollowUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserProfileDetailViewModel(
                    userId,
                    getExploreUsersUseCase,
                    toggleUserFollowUseCase
                ) as T
            }
        }
    }
}
