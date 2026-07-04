package com.example.feedbook.features.readingrooms.data.remote

import com.example.feedbook.core.network.ApiService
import com.example.feedbook.features.readingrooms.data.remote.dto.ChangeReadingRoomBookRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.CreateReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.DeleteReadingRoomRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomCommentRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.SaveReadingRoomRatingRequestDto
import com.example.feedbook.features.readingrooms.data.remote.dto.UpdateReadingRoomDescriptionRequestDto

class ReadingRoomsRemoteDataSource(private val api: ApiService) {
    suspend fun listRooms() = api.getReadingRooms()
    suspend fun getRoom(id: String) = api.getReadingRoom(id)
    suspend fun createRoom(name: String, description: String, shortDescription: String, isAdult: Boolean) =
        api.createReadingRoom(CreateReadingRoomRequestDto(name, description, shortDescription, isAdult))
    suspend fun joinRoom(id: String) = api.joinReadingRoom(id)
    suspend fun leaveRoom(id: String) = api.leaveReadingRoom(id)
    suspend fun updateDescription(id: String, description: String) =
        api.updateReadingRoomDescription(id, UpdateReadingRoomDescriptionRequestDto(description))
    suspend fun deleteRoom(id: String, confirmationName: String) =
        api.deleteReadingRoom(id, DeleteReadingRoomRequestDto(confirmationName))
    suspend fun kickMember(id: String, userId: String) = api.kickReadingRoomMember(id, userId)
    suspend fun followedBooks() = api.getFollowedBooks()
    suspend fun changeBook(id: String, bookId: String) =
        api.changeReadingRoomBook(id, ChangeReadingRoomBookRequestDto(bookId))
    suspend fun rate(id: String, rating: Float) =
        api.saveReadingRoomRating(id, SaveReadingRoomRatingRequestDto(rating))
    suspend fun comment(id: String, text: String, parentCommentId: String? = null) =
        api.saveReadingRoomComment(id, SaveReadingRoomCommentRequestDto(text, parentCommentId))
}
