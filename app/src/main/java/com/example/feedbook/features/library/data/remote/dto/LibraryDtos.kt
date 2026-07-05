package com.example.feedbook.features.library.data.remote.dto

import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
import com.example.feedbook.features.profile.data.remote.dto.CurrentBookDto
import com.example.feedbook.features.profile.data.remote.dto.LibraryBookDto

data class LibraryDto(
    val title: String,
    val subtitle: String,
    val avatar: AvatarDto,
    val currentBook: CurrentBookDto,
    val readingBooks: List<LibraryBookDto>,
    val shelfBooks: List<LibraryBookDto>,
    val completedBooks: Int,
    val readHistory: List<ReadBookDto>
)

data class ReadBookDto(
    val id: String,
    val title: String,
    val author: String,
    val startedOn: String,
    val finishedOn: String,
    val personalRating: Float,
    val coverAccentHex: Long,
    val coverImageUrl: String?
)
