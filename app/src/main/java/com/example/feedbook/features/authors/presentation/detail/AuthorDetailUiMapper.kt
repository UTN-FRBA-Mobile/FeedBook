package com.example.feedbook.features.authors.presentation.detail

import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.domain.model.Book

fun Author.toUiModel(isFollowing: Boolean = false): AuthorUiModel = AuthorUiModel(
    id = id,
    name = name,
    lifespan = "$birthYear – ${deathYear ?: "presente"}",
    description = description,
    biography = biography,
    imageUrl = imageUrl,
    books = books.map { it.toUiModel() },
    followersText = "$followers lectores siguen a este autor",
    isFollowing = isFollowing
)

fun Book.toUiModel(): AuthorBookUiModel = AuthorBookUiModel(
    id = id,
    title = title,
    coverUrl = coverImageUrl,
    genreAndYear = "$genre · $published"
)