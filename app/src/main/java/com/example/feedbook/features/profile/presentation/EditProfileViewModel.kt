package com.example.feedbook.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.model.UpdateProfileCommand
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditProfileViewModel(
    observeOwnProfileUseCase: ObserveOwnProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(sampleProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                _state.value = profile.toOwnProfileUiState()
            }
        }
    }

    fun saveProfile(updatedState: ProfileUiState, onSaved: () -> Unit) {
        viewModelScope.launch {
            updateProfileUseCase(
                UpdateProfileCommand(
                    name = updatedState.name,
                    handle = updatedState.handle,
                    quote = updatedState.quote,
                    avatarTopColorHex = updatedState.avatarStyle.topColor.value.toLong(),
                    avatarBottomColorHex = updatedState.avatarStyle.bottomColor.value.toLong(),
                    avatarImageUri = updatedState.avatarImageUri,
                    targetPagesPerDay = updatedState.readingGoal?.targetPagesPerDay
                )
            )
            onSaved()
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
