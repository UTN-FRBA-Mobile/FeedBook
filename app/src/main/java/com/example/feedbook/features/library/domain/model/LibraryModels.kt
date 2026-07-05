package com.example.feedbook.features.library.domain.model

data class ReaderLibrary(
    val title: String,
    val subtitle: String,
    val avatar: LibraryAvatar,
    val currentBook: LibraryCurrentBook,
    val readingBooks: List<LibraryShelfBook>,
    val shelfBooks: List<LibraryShelfBook>,
    val completedBooks: Int,
    val readHistory: List<ReadBookEntry>
)

data class LibraryAvatar(
    val topColorHex: Long,
    val bottomColorHex: Long,
    val imageUri: String?
)

data class LibraryCurrentBook(
    val id: String,
    val title: String,
    val author: String,
    val page: Int,
    val totalPages: Int,
    val progress: Float,
    val coverImageUrl: String?
)

data class LibraryShelfBook(
    val id: String,
    val title: String,
    val coverImageUrl: String?
)

data class ReadBookEntry(
    val id: String,
    val title: String,
    val author: String,
    val startedOn: String,
    val finishedOn: String,
    val personalRating: Float,
    val coverAccentHex: Long,
    val coverImageUrl: String?
)
