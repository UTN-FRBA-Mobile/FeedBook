package com.example.feedbook.features.books.domain.model

data class FriendReading(
    val userId: String,
    val name: String,
    val handle: String,
    val avatarImageUrl: String?,
    val avatarTopColorHex: Long,
    val avatarBottomColorHex: Long,
    val currentPage: Int,
    val totalPages: Int,
    val progress: Float
)
