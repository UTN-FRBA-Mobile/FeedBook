package com.example.feedbook.features.auth.domain.model

data class AuthSession(
    val username: String,
    val token: String,
    val easyLogin: Boolean
)
