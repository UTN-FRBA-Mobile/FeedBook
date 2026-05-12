package com.example.feedbook.core.network

object BackendUrls {
    fun origin(rawOrigin: String): String {
        val trimmed = rawOrigin.trim()
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }

    fun apiBaseUrl(rawOrigin: String): String = "${origin(rawOrigin)}api/"
}
