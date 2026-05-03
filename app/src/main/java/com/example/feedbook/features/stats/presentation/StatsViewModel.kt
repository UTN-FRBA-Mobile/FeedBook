package com.example.feedbook.features.stats.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.profile.domain.model.ReaderProfile
import com.example.feedbook.features.stats.domain.model.ReadingStats
import com.example.feedbook.features.stats.domain.usecase.GetStatsUseCase
import com.example.feedbook.features.profile.domain.usecase.ObserveOwnProfileUseCase
import com.example.feedbook.features.profile.presentation.AvatarStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatsViewModel(
    private val getStatsUseCase: GetStatsUseCase,
    observeOwnProfileUseCase: ObserveOwnProfileUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(sampleStatsUiState())
    val state: StateFlow<StatsUiState> = _state.asStateFlow()

    private var baseStats: ReadingStats? = null
    private var currentAvatarStyle: AvatarStyle = sampleStatsUiState().avatarStyle
    private var currentAvatarImageUri: String? = null

    init {
        viewModelScope.launch {
            runCatching { getStatsUseCase() }
                .onSuccess { stats ->
                    baseStats = stats
                    emitStats(selectedMode = stats.radarSections.firstOrNull()?.mode ?: _state.value.selectedRadarMode)
                }
        }

        viewModelScope.launch {
            observeOwnProfileUseCase().collectLatest { profile ->
                currentAvatarStyle = profile.toAvatarStyle()
                currentAvatarImageUri = profile.avatar.imageUri
                emitStats(selectedMode = _state.value.selectedRadarMode)
            }
        }
    }

    fun selectMode(mode: String) {
        emitStats(selectedMode = mode)
    }

    private fun emitStats(selectedMode: String) {
        val stats = baseStats ?: return
        val mapped = stats.toUiState(
            avatarStyle = currentAvatarStyle,
            avatarImageUri = currentAvatarImageUri
        )
        val validMode = mapped.radarSections.firstOrNull { it.mode == selectedMode }?.mode
            ?: mapped.radarSections.firstOrNull()?.mode
            ?: selectedMode
        _state.value = mapped.copy(selectedRadarMode = validMode)
    }

    private fun ReaderProfile.toAvatarStyle(): AvatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    )

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
