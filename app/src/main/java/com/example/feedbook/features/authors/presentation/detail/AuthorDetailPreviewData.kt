package com.example.feedbook.features.authors.presentation.detail

object AuthorDetailPreviewData {
    val sampleState = AuthorDetailUiState(
        author = AuthorUiModel(
            id = "1",
            name = "Fiódor Dostoyevski",
            imageUrl = null,
            lifespan = "1821 – 1881",
            description = "Escritor ruso, considerado uno de los grandes novelistas de la literatura universal.",
            biography = "Nació en Moscú en 1821. Estudió ingeniería militar pero abandonó la carrera para dedicarse a la escritura...",
            isFollowing = false,
            followersText = "14.2k lectores siguen a este autor",
            books = listOf(
                AuthorBookUiModel("b1", "Crimen y Castigo", null, "Novela · 1866"),
                AuthorBookUiModel("b2", "El Idiota", null, "Novela · 1869"),
                AuthorBookUiModel("b3", "Los Hermanos Karamazov", null, "Novela · 1880"),
            )
        )
    )

    val loadingState = AuthorDetailUiState(isLoading = true)
}