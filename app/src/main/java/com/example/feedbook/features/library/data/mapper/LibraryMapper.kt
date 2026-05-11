package com.example.feedbook.features.library.data.mapper

import com.example.feedbook.features.library.data.remote.dto.LibraryDto
import com.example.feedbook.features.library.data.remote.dto.ReadBookDto
import com.example.feedbook.features.library.domain.model.LibraryAvatar
import com.example.feedbook.features.library.domain.model.LibraryCurrentBook
import com.example.feedbook.features.library.domain.model.LibraryShelfBook
import com.example.feedbook.features.library.domain.model.ReadBookEntry
import com.example.feedbook.features.library.domain.model.ReaderLibrary
import com.example.feedbook.features.profile.data.remote.dto.AvatarDto
import com.example.feedbook.features.profile.data.remote.dto.CurrentBookDto
import com.example.feedbook.features.profile.data.remote.dto.LibraryBookDto

fun LibraryDto.toDomain(): ReaderLibrary = ReaderLibrary(
    title = title,
    subtitle = subtitle,
    avatar = avatar.toDomain(),
    currentBook = currentBook.toDomain(),
    readingBooks = readingBooks.map(LibraryBookDto::toDomain),
    shelfBooks = shelfBooks.map(LibraryBookDto::toDomain),
    completedBooks = completedBooks,
    readHistory = readHistory.map(ReadBookDto::toDomain)
)

private fun AvatarDto.toDomain(): LibraryAvatar =
    LibraryAvatar(topColorHex, bottomColorHex, imageUri)

private fun CurrentBookDto.toDomain(): LibraryCurrentBook =
    LibraryCurrentBook(id, title, author, page, totalPages, progress, coverImageUrl)

private fun LibraryBookDto.toDomain(): LibraryShelfBook = LibraryShelfBook(title, coverImageUrl)

private fun ReadBookDto.toDomain(): ReadBookEntry = ReadBookEntry(
    title = title,
    author = author,
    startedOn = startedOn,
    finishedOn = finishedOn,
    personalRating = personalRating,
    coverAccentHex = coverAccentHex
)
