package com.example.feedbook.features.auth.domain.usecase

import com.example.feedbook.features.auth.domain.model.AuthSession
import com.example.feedbook.features.auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        secureLogin: Boolean
    ): AuthSession = repository.register(
        username = username,
        password = password,
        secureLogin = secureLogin
    )
}
