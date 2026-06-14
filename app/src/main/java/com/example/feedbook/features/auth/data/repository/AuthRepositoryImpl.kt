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
        secureLogin: Boolean
    ): AuthSession {
        val response = remoteDataSource.login(
            username = username,
            password = password,
            secureLogin = secureLogin
        )

        return AuthSession(
            username = username,
            token = response.token,
            secureLogin = secureLogin,
            expiresAtEpochSeconds = response.expiresAtEpochSeconds
        )
    }

    override suspend fun register(
        username: String,
        password: String,
        secureLogin: Boolean
    ): AuthSession {
        val response = remoteDataSource.register(
            username = username,
            password = password,
            secureLogin = secureLogin
        )

        return AuthSession(
            username = username,
            token = response.token,
            secureLogin = secureLogin,
            expiresAtEpochSeconds = response.expiresAtEpochSeconds
        )
    }
}
