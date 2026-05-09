package com.example.feedbook.features.auth.domain.repository

import com.example.feedbook.features.auth.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(
        username: String,
        password: String,
        easyLogin: Boolean
    ): AuthSession
}
