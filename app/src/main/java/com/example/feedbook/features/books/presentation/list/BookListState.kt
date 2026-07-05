package com.example.feedbook.features.books.presentation.list

import com.example.feedbook.features.authors.domain.model.Author
import com.example.feedbook.features.books.domain.model.Book
import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class BookListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val query: String = "",
    val selectedGenres: Set<String> = emptySet(),
    val selectedAuthors: Set<String> = emptySet(),
    val avatarStyle: AvatarStyle = defaultAvatarStyle(),
    val avatarPreset: AvatarPreset? = null,
    val avatarImageUri: String? = null,
    val books: List<Book> = emptyList(),
    val authors: List<Author> = emptyList(),
    val users: List<ExploreUser> = emptyList(),
    val error: String? = null
)
