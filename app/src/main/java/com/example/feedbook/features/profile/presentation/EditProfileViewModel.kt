package com.example.feedbook.features.profile.presentation

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val observeOwnProfileUseCase: ObserveOwnProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        emptyProfileUiState(ProfileVariant.OWN).copy(isLoading = true)
    )
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadProfile()
    }

    fun retry() {
        loadProfile()
    }

    fun saveProfile(updatedState: ProfileUiState, onSaved: () -> Unit) {
        viewModelScope.launch {
            updateProfileUseCase(
                UpdateProfileCommand(
                    name = updatedState.name,
                    handle = updatedState.handle,
                    quote = updatedState.quote,
                    avatarTopColorHex = updatedState.avatarStyle.topColor.toArgb().toLong(),
                    avatarBottomColorHex = updatedState.avatarStyle.bottomColor.toArgb().toLong(),
                    avatarPresetId = updatedState.avatarPreset?.id,
                    avatarImageUri = updatedState.avatarImageUri,
                    targetPagesPerDay = updatedState.readingGoal?.targetPagesPerDay
                )
            )
            onSaved()
        }
    }

    private fun loadProfile() {
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            observeOwnProfileUseCase()
                .catch { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collectLatest { profile ->
                    _state.value = profile.toOwnProfileUiState()
                }
        }
    }

    companion object {
        fun provideFactory(
            observeOwnProfileUseCase: ObserveOwnProfileUseCase,
            updateProfileUseCase: UpdateProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditProfileViewModel(
                    observeOwnProfileUseCase = observeOwnProfileUseCase,
                    updateProfileUseCase = updateProfileUseCase
                ) as T
            }
        }
    }
}
