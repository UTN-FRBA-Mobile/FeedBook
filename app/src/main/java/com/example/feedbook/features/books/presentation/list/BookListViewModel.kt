package com.example.feedbook.features.books.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetBooksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookListViewModel(
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BookListState(isLoading = true))
    val state: StateFlow<BookListState> = _state.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            runCatching { getBooksUseCase() }
                .onSuccess { books ->
                    _state.value = BookListState(books = books)
                }
                .onFailure { throwable ->
                    _state.value = BookListState(
                        error = throwable.message ?: "Unable to load books."
                    )
                }
        }
    }

    companion object {
        fun provideFactory(
            getBooksUseCase: GetBooksUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BookListViewModel(getBooksUseCase) as T
            }
        }
    }
}
