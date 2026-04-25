package com.example.feedbook.data.mapper

import com.example.feedbook.data.remote.dto.BookDto
import com.example.feedbook.domain.model.Book

fun BookDto.toDomain(): Book {
    return Book(
        id = id,
        title = title,
        author = author,
        description = description
    )
}
