package com.example.feedbook.core.network

import com.example.feedbook.features.auth.data.remote.dto.LoginRequestDto
import com.example.feedbook.features.auth.data.remote.dto.LoginResponseDto
import com.example.feedbook.features.auth.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @POST("register")
    suspend fun register(@Body body: RegisterRequestDto): LoginResponseDto
}
