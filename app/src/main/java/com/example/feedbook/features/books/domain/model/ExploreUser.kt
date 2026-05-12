package com.example.feedbook.features.books.domain.model

data class ExploreUser(
    val id: String,
    val name: String,
    val handle: String,
    val bio: String,
    val avatarImageUrl: String?,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val followersLabel: String,
    val booksReadLabel: String
)
