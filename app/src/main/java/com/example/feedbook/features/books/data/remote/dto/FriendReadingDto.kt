package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FriendReadingDto(
    @SerializedName("user_id")                val userId: String,
    @SerializedName("name")                   val name: String,
    @SerializedName("handle")                 val handle: String,
    @SerializedName("avatarImageUrl")         val avatarImageUrl: String?,
    @SerializedName("avatarTopColorHex")      val avatarTopColorHex: Long,
    @SerializedName("avatarBottomColorHex")   val avatarBottomColorHex: Long,
    @SerializedName("current_page")           val currentPage: Int,
    @SerializedName("total_pages")            val totalPages: Int,
    @SerializedName("progress")               val progress: Float
)
