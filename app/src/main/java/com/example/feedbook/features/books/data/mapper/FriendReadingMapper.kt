package com.example.feedbook.features.books.data.mapper

import com.example.feedbook.features.books.data.remote.dto.FriendReadingDto
import com.example.feedbook.features.books.domain.model.FriendReading

fun FriendReadingDto.toDomain(): FriendReading {
    return FriendReading(
        userId = userId,
        name = name,
        handle = handle,
        avatarImageUrl = avatarImageUrl,
        avatarTopColorHex = avatarTopColorHex,
        avatarBottomColorHex = avatarBottomColorHex,
        currentPage = currentPage,
        totalPages = totalPages,
        progress = progress
    )
}
