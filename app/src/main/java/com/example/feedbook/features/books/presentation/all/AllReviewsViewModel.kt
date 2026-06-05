package com.example.feedbook.features.books.presentation.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.domain.usecase.GetReviewsUseCase
import com.example.feedbook.features.books.domain.usecase.ToggleLikeUseCase
import com.example.feedbook.features.books.presentation.detail.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllReviewsViewModel(
    private val bookId: String,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AllReviewsUiState(isLoading = true))
    val state: StateFlow<AllReviewsUiState> = _state.asStateFlow()

    companion object {
        private const val PAGE_SIZE = 10
    }

    init {
        loadPage(1)
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoadingMore || s.reviews.size >= s.total) return
        loadPage(s.currentPage + 1)
    }

    private fun loadPage(page: Int) {
        val isFirst = page == 1
        _state.value = _state.value.copy(
            error = null,
            isLoading = if (isFirst) true else _state.value.isLoading,
            isLoadingMore = if (!isFirst) true else _state.value.isLoadingMore
        )

        viewModelScope.launch {
            runCatching {
                getReviewsUseCase(bookId, page, PAGE_SIZE)
            }.onSuccess { (reviews, total) ->
                val current = _state.value
                _state.value = current.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    reviews = if (isFirst) reviews.map { it.toUiModel() } else current.reviews + reviews.map { it.toUiModel() },
                    total = total,
                    currentPage = page
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = e.message ?: "Error loading reviews"
                )
            }
        }
    }

    fun toggleLike(reviewId: String) {
        val currentList = _state.value.reviews
        val idx = currentList.indexOfFirst { it.id == reviewId }
        if (idx == -1) return

        val review = currentList[idx]
        val newLiked = !review.isLikedByMe
        val newLikes = if (newLiked) review.likes + 1 else review.likes - 1
        val patched = review.copy(isLikedByMe = newLiked, likes = newLikes,
            likesText = if (newLikes == 1) "1 like" else "$newLikes likes")

        _state.value = _state.value.copy(
            reviews = currentList.toMutableList().also { it[idx] = patched }
        )

        viewModelScope.launch {
            runCatching { toggleLikeUseCase(bookId, reviewId) }
                .onSuccess { updated ->
                    val list = _state.value.reviews.toMutableList()
                    val uiIdx = list.indexOfFirst { it.id == reviewId }
                    if (uiIdx != -1) {
                        list[uiIdx] = updated.toUiModel()
                        _state.value = _state.value.copy(reviews = list)
                    }
                }
                .onFailure {
                    val revertList = _state.value.reviews.toMutableList()
                    if (idx < revertList.size) {
                        revertList[idx] = review
                        _state.value = _state.value.copy(reviews = revertList)
                    }
                }
        }
    }

    class Factory(
        private val bookId: String,
        private val getReviewsUseCase: GetReviewsUseCase,
        private val toggleLikeUseCase: ToggleLikeUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AllReviewsViewModel(bookId, getReviewsUseCase, toggleLikeUseCase) as T
    }
}
