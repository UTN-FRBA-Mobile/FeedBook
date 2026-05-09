package com.example.feedbook.features.auth.domain.model

data class AuthSession(
    val username: String,
    val token: String,
    val secureLogin: Boolean,
    val expiresAtEpochSeconds: Long
)
