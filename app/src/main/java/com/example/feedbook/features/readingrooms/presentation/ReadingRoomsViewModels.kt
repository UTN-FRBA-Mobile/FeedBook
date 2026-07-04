package com.example.feedbook.features.readingrooms.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.readingrooms.data.remote.ReadingRoomsRemoteDataSource
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomDetailDto
import com.example.feedbook.features.readingrooms.data.remote.dto.ReadingRoomListDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReadingRoomListState(
    val isLoading: Boolean = true,
    val query: String = "",
    val rooms: ReadingRoomListDto? = null,
    val error: String? = null
)

class ReadingRoomListViewModel(
    private val remote: ReadingRoomsRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(ReadingRoomListState())
    val state: StateFlow<ReadingRoomListState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { remote.listRooms() }
                .onSuccess { _state.value = _state.value.copy(isLoading = false, rooms = it) }
                .onFailure { _state.value = _state.value.copy(isLoading = false, error = it.message ?: "Unable to load the groups") }
        }
    }

    fun updateQuery(value: String) {
        _state.value = _state.value.copy(query = value)
    }

    fun createRoom(name: String, description: String, shortDescription: String, isAdult: Boolean, onCreated: (String) -> Unit) {
        viewModelScope.launch {
            runCatching { remote.createRoom(name, description, shortDescription, isAdult) }
                .onSuccess { onCreated(it.id) }
                .onFailure { _state.value = _state.value.copy(error = it.message ?: "Unable to create the group") }
        }
    }

    companion object {
        fun provideFactory(remote: ReadingRoomsRemoteDataSource): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ReadingRoomListViewModel(remote) as T
            }
    }
}

data class ReadingRoomState(
    val isLoading: Boolean = true,
    val room: ReadingRoomDetailDto? = null,
    val followedBooks: List<BookDto> = emptyList(),
    val error: String? = null,
    val feedback: String? = null
)

class ReadingRoomViewModel(
    private val roomId: String,
    private val remote: ReadingRoomsRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(ReadingRoomState())
    val state: StateFlow<ReadingRoomState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val roomResult = runCatching { remote.getRoom(roomId) }
            val booksResult = runCatching { remote.followedBooks() }
            _state.value = _state.value.copy(
                isLoading = false,
                room = roomResult.getOrNull(),
                followedBooks = booksResult.getOrDefault(emptyList()),
                error = roomResult.exceptionOrNull()?.message
            )
        }
    }

    fun join() = updateRoom { remote.joinRoom(roomId) }
    fun changeBook(bookId: String) = updateRoom("You cannot activate the same book again") { remote.changeBook(roomId, bookId) }
    fun rate(rating: Float) = updateRoom { remote.rate(roomId, rating) }
    fun comment(text: String, parentCommentId: String? = null) = updateRoom { remote.comment(roomId, text, parentCommentId) }
    fun updateDescription(description: String) = updateRoom { remote.updateDescription(roomId, description) }

    fun kick(userId: String) {
        viewModelScope.launch {
            runCatching { remote.kickMember(roomId, userId) }
                .onSuccess { load() }
                .onFailure { _state.value = _state.value.copy(feedback = it.message ?: "Unable to remove the member") }
        }
    }

    fun delete(confirmationName: String, onDeleted: () -> Unit) {
        viewModelScope.launch {
            runCatching { remote.deleteRoom(roomId, confirmationName) }
                .onSuccess { onDeleted() }
                .onFailure { _state.value = _state.value.copy(feedback = "Type the full name to delete the group") }
        }
    }

    fun clearFeedback() {
        _state.value = _state.value.copy(feedback = null)
    }

    private fun updateRoom(conflictMessage: String = "Unable to update the group", block: suspend () -> ReadingRoomDetailDto) {
        viewModelScope.launch {
            runCatching { block() }
                .onSuccess { _state.value = _state.value.copy(room = it, feedback = null) }
                .onFailure { _state.value = _state.value.copy(feedback = conflictMessage) }
        }
    }

    companion object {
        fun provideFactory(roomId: String, remote: ReadingRoomsRemoteDataSource): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ReadingRoomViewModel(roomId, remote) as T
            }
    }
}
