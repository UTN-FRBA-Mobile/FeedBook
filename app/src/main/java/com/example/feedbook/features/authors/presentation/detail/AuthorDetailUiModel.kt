package com.example.feedbook.features.authors.presentation.detail

import com.example.feedbook.features.books.domain.model.ExploreUser
import com.example.feedbook.features.profile.presentation.AvatarPreset
import com.example.feedbook.features.profile.presentation.AvatarStyle
import com.example.feedbook.features.profile.presentation.defaultAvatarStyle

data class AuthorDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val author: AuthorUiModel? = null,
    val avatarStyle: AvatarStyle = defaultAvatarStyle(),
    val avatarPreset: AvatarPreset? = null,
    val avatarImageUri: String? = null,
    val authorUsers: List<ExploreUser> = emptyList()
)
data class AuthorUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val lifespan: String,
    val description: String,
    val biography: String,
    val isFollowing: Boolean,
    val followersText: String,
    val books: List<AuthorBookUiModel>
)

data class AuthorBookUiModel(
    val id: String,
    val title: String,
    val coverUrl: String?,
    val genreAndYear: String
)