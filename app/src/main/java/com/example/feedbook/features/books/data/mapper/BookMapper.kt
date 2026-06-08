package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.BookDto
import com.example.feedbook.features.books.data.remote.dto.ExploreUserDto
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser

fun BookDto.toDomain(): Book {
    return Book(
        id = id,
        authorId = authorId,
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

fun ExploreUserDto.toDomain(): ExploreUser {
    return ExploreUser(
        id = id,
        name = name,
        handle = handle,
        bio = bio,
        avatarImageUrl = avatarImageUrl,
        avatarTopColorHex = avatarTopColorHex,
        avatarBottomColorHex = avatarBottomColorHex,
        followersLabel = followersLabel,
        booksReadLabel = booksReadLabel
    )
}
