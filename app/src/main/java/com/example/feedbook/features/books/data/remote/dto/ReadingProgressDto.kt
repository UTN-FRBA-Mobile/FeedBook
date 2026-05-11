package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReadingProgressDto(
    @SerializedName("book_id")     val bookId: String,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total_pages")  val totalPages: Int,
    @SerializedName("updated_at")   val updatedAt: String
)