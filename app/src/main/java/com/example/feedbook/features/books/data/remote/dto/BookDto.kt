package com.example.feedbook.features.books.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id") val id: String,
    @SerializedName("author_id") val authorId: String? = null,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("description") val description: String,
    @SerializedName("cover_image_url") val coverImageUrl: String? = null,
    @SerializedName("pages") val pages: Int,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("language") val language: String,
    @SerializedName("published") val published: String,
)
