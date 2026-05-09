package com.example.feedbook.features.auth.data.remote

import com.example.feedbook.core.network.AuthApiService
import com.example.feedbook.features.auth.data.remote.dto.LoginRequestDto
import com.example.feedbook.features.auth.data.remote.dto.LoginResponseDto

class AuthRemoteDataSource(
    private val authApiService: AuthApiService
) {
    suspend fun login(
        username: String,
        password: String,
        easyLogin: Boolean
    ): LoginResponseDto = authApiService.login(
        LoginRequestDto(
            username = username,
            password = password,
            easyLogin = easyLogin
        )
    )
}
