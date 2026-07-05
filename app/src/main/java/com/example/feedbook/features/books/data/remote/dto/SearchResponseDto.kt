package com.example.feedbook.features.books.data.remote.dto

import com.example.feedbook.features.authors.data.remote.dto.AuthorDto

data class SearchResponseDto(
    val books: List<BookDto> = emptyList(),
    val authors: List<AuthorDto> = emptyList(),
    val users: List<ExploreUserDto> = emptyList()
)
