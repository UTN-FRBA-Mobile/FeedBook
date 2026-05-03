package com.example.feedbook.features.stats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.stats.domain.model.ReadingStats
import com.example.feedbook.features.stats.domain.usecase.GetStatsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarPresentation
import com.example.feedbook.features.profile.presentation.toAvatarPresentation
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatsViewModel(
    private val getStatsUseCase: GetStatsUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(
        emptyStatsUiState().copy(isLoading = true)
    )
    val state: StateFlow<StatsUiState> = _state.asStateFlow()

    private var baseStats: ReadingStats? = null
    private var avatarPresentation = AvatarPresentation(
        style = defaultAvatarStyle(),
        imageUri = null
    )

    init {
        loadStats()

        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                avatarPresentation = profile.toAvatarPresentation()
                emitStats(selectedMode = _state.value.selectedRadarMode)
            }
        }
    }

    fun retry() {
        loadStats()
    }

    fun selectMode(mode: String) {
        emitStats(selectedMode = mode)
    }

    private fun loadStats() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { getStatsUseCase() }
                .onSuccess { stats ->
                    baseStats = stats
                    emitStats(selectedMode = stats.radarSections.firstOrNull()?.mode ?: _state.value.selectedRadarMode)
                }
                .onFailure { throwable ->
                    _state.value = emptyStatsUiState().copy(
                        avatarStyle = avatarPresentation.style,
                        avatarImageUri = avatarPresentation.imageUri,
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
        }
    }

    private fun emitStats(selectedMode: String) {
        val stats = baseStats ?: return
        val mapped = stats.toUiState(
            avatarStyle = avatarPresentation.style,
            avatarImageUri = avatarPresentation.imageUri
        )
        val validMode = mapped.radarSections.firstOrNull { it.mode == selectedMode }?.mode
            ?: mapped.radarSections.firstOrNull()?.mode
            ?: selectedMode
        _state.value = mapped.copy(
            selectedRadarMode = validMode,
            isLoading = false,
            errorMessage = null
        )
    }

    companion object {
        fun provideFactory(
            getStatsUseCase: GetStatsUseCase,
            observeOwnProfileUseCase: ObserveOwnProfileUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StatsViewModel(getStatsUseCase, observeOwnProfileUseCase) as T
            }
        }
    }
}
