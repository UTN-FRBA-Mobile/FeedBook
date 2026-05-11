package com.example.feedbook.features.authors.data.remote.dto

import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("id")           val id: String,
    @SerializedName("name")         val name: String,
    @SerializedName("birth_year")   val birthYear: Int,
    @SerializedName("death_year")   val deathYear: Int?,
    @SerializedName("nationality")  val nationality: String,
    @SerializedName("description")  val description: String,
    @SerializedName("biography")    val biography: String,
    @SerializedName("image_url")    val imageUrl: String?,
    @SerializedName("books")        val books: List<BookDto>,
    @SerializedName("followers")    val followers: Int
)