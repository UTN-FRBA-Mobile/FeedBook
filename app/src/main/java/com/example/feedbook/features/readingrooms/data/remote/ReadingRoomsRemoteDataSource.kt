package com.example.feedbook.features.readingrooms.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.core.state.UserContentRefreshBus
import com.example.feedbook.features.readingrooms.data.remote.dto.ChangeReadingRoomBookRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.CreateReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.DeleteReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomCommentRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomRatingRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.UpdateReadingRoomDescriptionRequestDto

class ReadingRoomsRemoteDataSource(
    private val api: ApiService,
    private val refreshBus: UserContentRefreshBus
) {
    suspend fun listRooms() = api.getReadingRooms()
    suspend fun getRoom(id: String) = api.getReadingRoom(id)
    suspend fun createRoom(name: String, description: String, shortDescription: String, isAdult: Boolean) =
        api.createReadingRoom(CreateReadingRoomRequestDto(name, description, shortDescription, isAdult)).also {
            refreshBus.refresh()
        }
    suspend fun joinRoom(id: String) = api.joinReadingRoom(id).also { refreshBus.refresh() }
    suspend fun leaveRoom(id: String) = api.leaveReadingRoom(id).also { refreshBus.refresh() }
    suspend fun updateDescription(id: String, description: String) =
        api.updateReadingRoomDescription(id, UpdateReadingRoomDescriptionRequestDto(description)).also {
            refreshBus.refresh()
        }
    suspend fun deleteRoom(id: String, confirmationName: String) =
        api.deleteReadingRoom(id, DeleteReadingRoomRequestDto(confirmationName)).also { refreshBus.refresh() }
    suspend fun kickMember(id: String, userId: String) =
        api.kickReadingRoomMember(id, userId).also { refreshBus.refresh() }
    suspend fun followedBooks() = api.getFollowedBooks()
    suspend fun changeBook(id: String, bookId: String) =
        api.changeReadingRoomBook(id, ChangeReadingRoomBookRequestDto(bookId)).also { refreshBus.refresh() }
    suspend fun rate(id: String, rating: Float) =
        api.saveReadingRoomRating(id, SaveReadingRoomRatingRequestDto(rating)).also { refreshBus.refresh() }
    suspend fun comment(id: String, text: String, parentCommentId: String? = null) =
        api.saveReadingRoomComment(id, SaveReadingRoomCommentRequestDto(text, parentCommentId)).also {
            refreshBus.refresh()
        }
}
