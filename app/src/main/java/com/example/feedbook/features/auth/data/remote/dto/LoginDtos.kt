package com.example.feedbook.features.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("easy_login")
    val easyLogin: Boolean
)

data class LoginResponseDto(
    @SerializedName("token")
    val token: String
)
