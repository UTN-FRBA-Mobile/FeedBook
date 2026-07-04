package com.example.feedbook.features.authors.presentation.detail

object AuthorDetailPreviewData {
    val sampleState = AuthorDetailUiState(
        author = AuthorUiModel(
            id = "1",
            name = "Fyodor Dostoevsky",
            imageUrl = null,
            lifespan = "1821 – 1881",
            description = "Russian novelist, considered one of the great writers of world literature.",
            biography = "Born in Moscow in 1821. He studied military engineering but left it to devote himself to writing...",
            isFollowing = false,
            followersText = "14.2k readers follow this author",
            books = listOf(
                AuthorBookUiModel("b1", "Crime and Punishment", null, "Novel · 1866"),
                AuthorBookUiModel("b2", "The Idiot", null, "Novel · 1869"),
                AuthorBookUiModel("b3", "The Brothers Karamazov", null, "Novel · 1880"),
            )
        )
    )

    val loadingState = AuthorDetailUiState(isLoading = true)
}
