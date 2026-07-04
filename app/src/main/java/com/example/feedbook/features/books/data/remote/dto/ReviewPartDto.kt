package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewPartDto(
    @SerializedName("text") val text: String,
    @SerializedName("spoiler") val spoiler: Boolean
)
