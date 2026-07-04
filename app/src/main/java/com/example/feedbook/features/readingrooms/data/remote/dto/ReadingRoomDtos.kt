package com.example.feedbook.features.readingrooms.data.remote.dto

import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.google.gson.annotations.SerializedName

data class ReadingRoomListDto(
    val followed: List<ReadingRoomSummaryDto>,
    val other: List<ReadingRoomSummaryDto>
)

data class ReadingRoomSummaryDto(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("is_adult") val isAdult: Boolean,
    @SerializedName("creator_id") val creatorId: String,
    @SerializedName("creator_name") val creatorName: String,
    @SerializedName("creator_avatar_url") val creatorAvatarUrl: String?,
    @SerializedName("member_count") val memberCount: Int,
    @SerializedName("is_member") val isMember: Boolean,
    @SerializedName("active_book") val activeBook: BookDto?,
    @SerializedName("active_since") val activeSince: String?
)

data class ReadingRoomDetailDto(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("is_adult") val isAdult: Boolean,
    @SerializedName("creator_id") val creatorId: String,
    @SerializedName("creator_name") val creatorName: String,
    @SerializedName("creator_avatar_url") val creatorAvatarUrl: String?,
    @SerializedName("member_count") val memberCount: Int,
    @SerializedName("is_member") val isMember: Boolean,
    @SerializedName("active_book") val activeBook: BookDto?,
    @SerializedName("active_since") val activeSince: String?,
    val members: List<ReadingRoomMemberDto>,
    val feed: List<ReadingRoomFeedItemDto>,
    @SerializedName("active_period") val activePeriod: ReadingRoomPeriodDto?,
    val history: List<ReadingRoomPeriodDto>
)

data class ReadingRoomMemberDto(
    @SerializedName("user_id") val userId: String,
    val name: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("is_admin") val isAdmin: Boolean,
    @SerializedName("joined_at") val joinedAt: String
)

data class ReadingRoomPeriodDto(
    val id: String,
    val book: BookDto,
    @SerializedName("started_at") val startedAt: String,
    @SerializedName("ended_at") val endedAt: String?,
    @SerializedName("average_rating") val averageRating: Float,
    @SerializedName("my_rating") val myRating: Float?,
    val ratings: List<ReadingRoomRatingDto>,
    val feed: List<ReadingRoomFeedItemDto>
)

data class ReadingRoomRatingDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    val rating: Float,
    @SerializedName("created_at") val createdAt: String
)

data class ReadingRoomFeedItemDto(
    val type: String,
    val comment: ReadingRoomCommentDto?,
    val event: ReadingRoomEventDto?
)

data class ReadingRoomCommentDto(
    val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    val text: String,
    @SerializedName("created_at") val createdAt: String,
    val replies: List<ReadingRoomCommentDto>
)

data class ReadingRoomEventDto(
    val id: String,
    val type: String,
    val text: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("actor_id") val actorId: String?,
    @SerializedName("actor_name") val actorName: String?
)

data class CreateReadingRoomRequestDto(
    val name: String,
    val description: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("is_adult") val isAdult: Boolean
)

data class UpdateReadingRoomDescriptionRequestDto(val description: String)
data class DeleteReadingRoomRequestDto(@SerializedName("confirmation_name") val confirmationName: String)
data class ChangeReadingRoomBookRequestDto(@SerializedName("book_id") val bookId: String)
data class SaveReadingRoomRatingRequestDto(val rating: Float)
data class SaveReadingRoomCommentRequestDto(
    val text: String,
    @SerializedName("parent_comment_id") val parentCommentId: String? = null
)
