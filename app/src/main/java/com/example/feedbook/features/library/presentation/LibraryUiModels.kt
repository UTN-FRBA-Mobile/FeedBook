package com.example.feedbook.features.library.presentation

import androidx.compose.ui.graphics.Color
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.CurrentBook
import com.example.feedbook.features.profile.presentation.LibraryBook
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class LibraryUiState(
    val title: String,
    val subtitle: String,
    val avatarStyle: AvatarStyle,
    val avatarPreset: AvatarPreset?,
    val avatarImageUri: String?,
    val currentBook: CurrentBook,
    val readingBooks: List<LibraryBook>,
    val shelfBooks: List<LibraryBook>,
    val completedBooks: Int,
    val readHistory: List<ReadBookItem>,
    val followedAuthors: List<FollowedAuthorUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val readingCount: Int get() = readingBooks.size
    val shelfCount: Int get() = shelfBooks.size
}

data class FollowedAuthorUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?
)

data class ReadBookItem(
    val title: String,
    val author: String,
    val startedOn: String,
    val finishedOn: String,
    val personalRating: Int,
    val coverAccent: Color,
    val coverImageUrl: String?
)

fun sampleLibraryUiState(): LibraryUiState = LibraryUiState(
    title = "My Library",
    subtitle = "Your personal collection, current read, and completed shelf.",
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null,
    currentBook = CurrentBook(
        id = "2",
        title = "The Secret History",
        author = "Donna Tartt",
        page = 248,
        totalPages = 559,
        progress = 0.44f,
        coverImageUrl = "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"
    ),
    readingBooks = listOf(
        LibraryBook("2", "The Secret History", "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"),
        LibraryBook("3", "Foucault's Pendulum", "https://covers.openlibrary.org/b/isbn/9780156032971-L.jpg"),
        LibraryBook("4", "If on a winter's night a traveler", "https://covers.openlibrary.org/b/isbn/9780156439619-L.jpg")
    ),
    shelfBooks = listOf(
        LibraryBook("2", "The Secret History", "https://covers.openlibrary.org/b/isbn/9781400031702-L.jpg"),
        LibraryBook("5", "Fictions", "https://covers.openlibrary.org/b/isbn/9780802130303-L.jpg"),
        LibraryBook("3", "Never Let Me Go", "https://covers.openlibrary.org/b/isbn/9781400078776-L.jpg"),
        LibraryBook("4", "Beloved", "https://covers.openlibrary.org/b/isbn/9781400033416-L.jpg"),
        LibraryBook("5", "Pale Fire", "https://covers.openlibrary.org/b/isbn/9780679723424-L.jpg"),
        LibraryBook("6", "The Waves", "https://covers.openlibrary.org/b/isbn/9780156949606-L.jpg")
    ),
    completedBooks = 142,
    readHistory = listOf(
        ReadBookItem("Beloved", "Toni Morrison", "Jan 12, 2026", "Jan 29, 2026", 5, Color(0xFF82645A), "https://covers.openlibrary.org/b/isbn/9781400033416-L.jpg"),
        ReadBookItem("Pale Fire", "Vladimir Nabokov", "Feb 02, 2026", "Feb 18, 2026", 4, Color(0xFF627A92), "https://covers.openlibrary.org/b/isbn/9780679723424-L.jpg"),
        ReadBookItem("The Waves", "Virginia Woolf", "Mar 03, 2026", "Mar 21, 2026", 5, Color(0xFF6C8A80), "https://covers.openlibrary.org/b/isbn/9780156949606-L.jpg"),
        ReadBookItem("Never Let Me Go", "Kazuo Ishiguro", "Apr 01, 2026", "Apr 14, 2026", 4, Color(0xFF536E8A), "https://covers.openlibrary.org/b/isbn/9781400078776-L.jpg")
    ),
    followedAuthors = emptyList(),
    isLoading = false,
    errorMessage = null
)

fun emptyLibraryUiState(): LibraryUiState = LibraryUiState(
    title = "",
    subtitle = "",
    avatarStyle = defaultAvatarStyle(),
    avatarPreset = null,
    avatarImageUri = null,
    currentBook = CurrentBook("", "", "", 0, 0, 0f, null),
    readingBooks = emptyList(),
    shelfBooks = emptyList(),
    completedBooks = 0,
    readHistory = emptyList(),
    followedAuthors = emptyList(),
    isLoading = false,
    errorMessage = null
)
