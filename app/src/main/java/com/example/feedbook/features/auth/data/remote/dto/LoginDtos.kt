package com.example.feedbook.features.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("secure_login")
    val secureLogin: Boolean
)

data class LoginResponseDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("exp")
    val expiresAtEpochSeconds: Long
)

data class RegisterRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("secure_login")
    val secureLogin: Boolean
)
