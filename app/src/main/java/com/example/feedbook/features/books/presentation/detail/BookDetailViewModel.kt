package com.example.feedbook.features.books.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetBookByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookId: String,
    private val getBookByIdUseCase: GetBookByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookDetailState(isLoading = true))
    val state: StateFlow<BookDetailState> = _state.asStateFlow()

    init {
        loadBook()
    }

    fun loadBook() {
        viewModelScope.launch {
            _state.value = BookDetailState(isLoading = true)

            runCatching { getBookByIdUseCase(bookId) }
                .onSuccess { book ->
                    _state.value = BookDetailState(book = book)
                }
                .onFailure { throwable ->
                    _state.value = BookDetailState(
                        error = throwable.message ?: "Unable to load book details."
                    )
                }
        }
    }

    companion object {
        fun provideFactory(
            bookId: String,
            getBookByIdUseCase: GetBookByIdUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookDetailViewModel(bookId, getBookByIdUseCase) as T
            }
        }
    }
}
