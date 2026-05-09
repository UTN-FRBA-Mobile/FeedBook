package com.example.feedbook.core.session

import android.util.Base64
import com.example.feedbook.features.auth.domain.model.AuthSession
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

object JwtSessionParser {
    private val gson = Gson()

    fun parse(token: String): AuthSession? {
        val segments = token.split(".")
        if (segments.size != 3) {
            return null
        }

        return runCatching {
            val payloadJson = Base64.decode(
                segments[1],
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            ).decodeToString()
            val payload = gson.fromJson(payloadJson, JwtPayload::class.java)

            AuthSession(
                username = payload.username,
                token = token,
                secureLogin = payload.secureLogin,
                expiresAtEpochSeconds = payload.expiresAtEpochSeconds
            )
        }.getOrNull()
    }

    private data class JwtPayload(
        @SerializedName("username")
        val username: String,
        @SerializedName("secure_login")
        val secureLogin: Boolean,
        @SerializedName("exp")
        val expiresAtEpochSeconds: Long
    )
}
