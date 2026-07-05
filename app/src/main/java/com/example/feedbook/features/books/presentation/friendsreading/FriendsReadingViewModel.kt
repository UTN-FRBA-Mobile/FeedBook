package com.example.feedbook.features.books.presentation.friendsreading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.model.FriendReading
import com.example.feedbook.features.books.domain.usecase.GetFriendsReadingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FriendsReadingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val friends: List<FriendReading> = emptyList()
)

class FriendsReadingViewModel(
    private val bookId: String,
    private val getFriendsReadingUseCase: GetFriendsReadingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FriendsReadingUiState(isLoading = true))
    val state: StateFlow<FriendsReadingUiState> = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = FriendsReadingUiState(isLoading = true)
            runCatching { getFriendsReadingUseCase(bookId) }
                .onSuccess { friends ->
                    _state.value = FriendsReadingUiState(friends = friends)
                }
                .onFailure { e ->
                    _state.value = FriendsReadingUiState(
                        error = e.message ?: "Error loading friends reading"
                    )
                }
        }
    }

    class Factory(
        private val bookId: String,
        private val getFriendsReadingUseCase: GetFriendsReadingUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FriendsReadingViewModel(bookId, getFriendsReadingUseCase) as T
    }
}
