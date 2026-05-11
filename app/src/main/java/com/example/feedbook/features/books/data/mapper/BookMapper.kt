package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.domain.model.Book

fun BookDto.toDomain(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        description = description,
        coverImageUrl = coverImageUrl,
        pages = pages,
        genre = genre,
        language = language,
        published = published,
        isbn = isbn
    )
}