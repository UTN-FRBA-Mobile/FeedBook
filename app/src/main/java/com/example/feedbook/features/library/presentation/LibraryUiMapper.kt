package com.example.feedbook.features.library.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.library.domain.model.ReaderLibrary
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.CurrentBook
import com.example.feedbook.features.profile.presentation.LibraryBook

fun ReaderLibrary.toUiState(): LibraryUiState = LibraryUiState(
    title = title,
    subtitle = subtitle,
    avatarStyle = AvatarStyle(
        topColor = Color(avatar.topColorHex),
        bottomColor = Color(avatar.bottomColorHex)
    ),
    avatarImageUri = avatar.imageUri,
    currentBook = CurrentBook(
        title = currentBook.title,
        author = currentBook.author,
        page = currentBook.page,
        totalPages = currentBook.totalPages,
        progress = currentBook.progress,
        coverAccent = Color(currentBook.coverAccentHex)
    ),
    readingBooks = readingBooks.map { LibraryBook(it.title, Color(it.accentHex)) },
    shelfBooks = shelfBooks.map { LibraryBook(it.title, Color(it.accentHex)) },
    completedBooks = completedBooks,
    readHistory = readHistory.map {
        ReadBookItem(
            title = it.title,
            author = it.author,
            startedOn = it.startedOn,
            finishedOn = it.finishedOn,
            personalRating = it.personalRating,
            coverAccent = Color(it.coverAccentHex)
        )
    }
)
