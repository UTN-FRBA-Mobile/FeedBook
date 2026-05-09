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
        coverImageUrl = "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"
    ),
    readingBooks = listOf(
        LibraryBook("The Secret History", "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"),
        LibraryBook("Foucault's Pendulum", "https://covers.openlibrary.org/b/isbn/9780156032971-L.jpg"),
        LibraryBook("If on a winter's night a traveler", "https://covers.openlibrary.org/b/isbn/9780156439619-L.jpg")
    ),
    shelfBooks = listOf(
        LibraryBook("The Secret History", "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"),
        LibraryBook("Fictions", "https://covers.openlibrary.org/b/isbn/9780802130303-L.jpg"),
        LibraryBook("Never Let Me Go", "https://covers.openlibrary.org/b/isbn/9781400078776-L.jpg"),
        LibraryBook("Beloved", "https://covers.openlibrary.org/b/isbn/9781400033416-L.jpg"),
        LibraryBook("Pale Fire", "https://covers.openlibrary.org/b/isbn/9780679723424-L.jpg"),
        LibraryBook("The Waves", "https://covers.openlibrary.org/b/isbn/9780156949606-L.jpg")
    ),
    completedBooks = 142,
    readHistory = listOf(
        ReadBookItem("Beloved", "Toni Morrison", "Jan 12, 2026", "Jan 29, 2026", 5, Color(0xFF82645A)),
        ReadBookItem("Pale Fire", "Vladimir Nabokov", "Feb 02, 2026", "Feb 18, 2026", 4, Color(0xFF627A92)),
        ReadBookItem("The Waves", "Virginia Woolf", "Mar 03, 2026", "Mar 21, 2026", 5, Color(0xFF6C8A80)),
        ReadBookItem("Never Let Me Go", "Kazuo Ishiguro", "Apr 01, 2026", "Apr 14, 2026", 4, Color(0xFF536E8A))
    )
)
