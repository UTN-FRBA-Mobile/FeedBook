package com.example.feedbook.features.authors.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.authors.domain.usecase.GetAuthorByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthorBooksUiState(
    val isLoading: Boolean = false,
    val authorName: String = "",
    val books: List<AuthorBookUiModel> = emptyList(),
    val error: String? = null
)

class AuthorBooksViewModel(
    private val authorId: String,
    private val authorName: String,
    private val getAuthorByIdUseCase: GetAuthorByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthorBooksUiState(isLoading = true, authorName = authorName))
    val state: StateFlow<AuthorBooksUiState> = _state.asStateFlow()

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            runCatching {
                getAuthorByIdUseCase(authorId)
            }.onSuccess { author ->
                _state.value = AuthorBooksUiState(
                    isLoading = false,
                    authorName = author.name,
                    books = author.books.map { it.toUiModel() }
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error loading books"
                )
            }
        }
    }

    class Factory(
        private val authorId: String,
        private val authorName: String,
        private val getAuthorByIdUseCase: GetAuthorByIdUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthorBooksViewModel(authorId, authorName, getAuthorByIdUseCase) as T
    }
}
