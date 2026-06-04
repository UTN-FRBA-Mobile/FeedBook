package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto (
    @SerializedName("id")              val id: String,
    @SerializedName("user_id")         val userId: String,
    @SerializedName("reviewer_name")   val reviewerName: String,
    @SerializedName("reviewer_avatar") val reviewerAvatar: String?,
    @SerializedName("rating")          val rating: Float,
    @SerializedName("text")            val text: String,
    @SerializedName("likes")           val likes: Int,
    @SerializedName("created_at")      val createdAt: String
)