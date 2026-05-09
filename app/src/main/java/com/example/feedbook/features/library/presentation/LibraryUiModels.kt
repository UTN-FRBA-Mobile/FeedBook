package com.example.feedbook.features.library.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.CurrentBook
import com.example.feedbook.features.profile.presentation.LibraryBook

data class LibraryUiState(
    val title: String,
    val subtitle: String,
    val avatarStyle: AvatarStyle,
    val avatarImageUri: String?,
    val currentBook: CurrentBook,
    val readingBooks: List<LibraryBook>,
    val shelfBooks: List<LibraryBook>,
    val completedBooks: Int,
    val readHistory: List<ReadBookItem>
) {
    val readingCount: Int get() = readingBooks.size
    val shelfCount: Int get() = shelfBooks.size
}

data class ReadBookItem(
    val title: String,
    val author: String,
    val startedOn: String,
    val finishedOn: String,
    val personalRating: Int,
    val coverAccent: Color
)

fun sampleLibraryUiState(): LibraryUiState = LibraryUiState(
    title = "My Library",
    subtitle = "Your personal collection, current read, and completed shelf.",
    avatarStyle = AvatarStyle(
        topColor = Color(0xFF315A73),
        bottomColor = Color(0xFFF0C6A8)
    ),
    avatarImageUri = null,
    currentBook = CurrentBook(
        title = "The Secret History",
        author = "Donna Tartt",
        page = 248,
        totalPages = 559,
        progress = 0.44f,
        coverAccent = Color(0xFF6E918B)
    ),
    readingBooks = listOf(
        LibraryBook("The Secret History", Color(0xFF6E918B)),
        LibraryBook("Foucault's Pendulum", Color(0xFF7A8F89)),
        LibraryBook("If on a winter's night a traveler", Color(0xFF8F745E))
    ),
    shelfBooks = listOf(
        LibraryBook("The Secret History", Color(0xFF6E918B)),
        LibraryBook("Fictions", Color(0xFF8C6B5A)),
        LibraryBook("Never Let Me Go", Color(0xFF536E8A)),
        LibraryBook("Beloved", Color(0xFF82645A)),
        LibraryBook("Pale Fire", Color(0xFF627A92)),
        LibraryBook("The Waves", Color(0xFF6C8A80))
    ),
    completedBooks = 142,
    readHistory = listOf(
        ReadBookItem("Beloved", "Toni Morrison", "Jan 12, 2026", "Jan 29, 2026", 5, Color(0xFF82645A)),
        ReadBookItem("Pale Fire", "Vladimir Nabokov", "Feb 02, 2026", "Feb 18, 2026", 4, Color(0xFF627A92)),
        ReadBookItem("The Waves", "Virginia Woolf", "Mar 03, 2026", "Mar 21, 2026", 5, Color(0xFF6C8A80)),
        ReadBookItem("Never Let Me Go", "Kazuo Ishiguro", "Apr 01, 2026", "Apr 14, 2026", 4, Color(0xFF536E8A))
    )
)
