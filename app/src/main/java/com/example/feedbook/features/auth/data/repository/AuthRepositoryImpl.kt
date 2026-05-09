package com.example.feedbook.features.auth.data.repository

import com.example.feedbook.features.auth.data.remote.AuthRemoteDataSource
import com.example.feedbook.features.auth.domain.model.AuthSession
import com.example.feedbook.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    override suspend fun login(
        username: String,
        password: String,
        easyLogin: Boolean
    ): AuthSession {
        val response = remoteDataSource.login(
            username = username,
            password = password,
            easyLogin = easyLogin
        )

        return AuthSession(
            username = username,
            token = response.token,
            easyLogin = easyLogin
        )
    }
}
