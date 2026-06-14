package com.example.feedbook.features.auth.data.remote

import com.example.feedbook.core.network.AuthApiService
import com.example.feedbook.features.auth.data.remote.dto.LoginRequestDto
import com.example.feedbook.features.auth.data.remote.dto.LoginResponseDto
import com.example.feedbook.features.auth.data.remote.dto.RegisterRequestDto

class AuthRemoteDataSource(
    private val authApiService: AuthApiService
) {
    suspend fun login(
        username: String,
        password: String,
        secureLogin: Boolean
    ): LoginResponseDto = authApiService.login(
        LoginRequestDto(
            username = username,
            password = password,
            secureLogin = secureLogin
        )
    )

    suspend fun register(
        username: String,
        password: String,
        secureLogin: Boolean
    ): LoginResponseDto = authApiService.register(
        RegisterRequestDto(
            username = username,
            password = password,
            secureLogin = secureLogin
        )
    )
}
