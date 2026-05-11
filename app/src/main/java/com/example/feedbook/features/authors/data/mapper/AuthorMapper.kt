package com.example.feedbook.features.authors.data.mapper

import com.example.feedbook.features.authors.data.remote.dto.AuthorDto
import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.data.mapper.toDomain

fun AuthorDto.toDomain(): Author {
    return Author(
        id = id,
        name = name,
        birthYear = birthYear,
        deathYear = deathYear,
        nationality = nationality,
        description = description,
        biography = biography,
        imageUrl = imageUrl,
        books = books.map { it.toDomain() },
        followers = followers
    )
}