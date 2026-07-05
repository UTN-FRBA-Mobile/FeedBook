package com.example.feedbook.features.books.domain.usecase

import com.example.feedbook.features.books.domain.model.FriendReading
import com.example.feedbook.features.books.domain.repository.BookRepository

class GetFriendsReadingUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(bookId: String): List<FriendReading> =
        repository.getFriendsReading(bookId)
}
