package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReviewsResponseDto(
    @SerializedName("reviews") val reviews: List<ReviewDto>,
    @SerializedName("total")   val total: Int
)
