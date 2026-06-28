package com.example.feedbook.core.network

import java.net.URI

object BackendUrls {
    fun origin(rawOrigin: String): String {
        val trimmed = rawOrigin.trim()
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }

    fun apiBaseUrl(rawOrigin: String): String = "${origin(rawOrigin)}api/"

    fun shouldBindToWifi(rawOrigin: String): Boolean {
        val host = URI(origin(rawOrigin)).host?.lowercase() ?: return false
        if (host == "localhost" || host == "10.0.2.2") {
            return false
        }

        val parts = host.split(".").map { it.toIntOrNull() }
        if (parts.size != 4 || parts.any { it == null || it !in 0..255 }) {
            return host.endsWith(".local")
        }

        val first = parts[0]!!
        val second = parts[1]!!
        return first == 10 ||
            first == 192 && second == 168 ||
            first == 172 && second in 16..31 ||
            first == 169 && second == 254
    }

    fun normalizeServerOrigin(rawOrigin: String): String {
        val trimmed = rawOrigin.trim()
        require(trimmed.isNotBlank()) { "Server address cannot be empty" }

        val candidate = if (trimmed.contains("://")) trimmed else "http://$trimmed"
        val uri = URI(candidate)
        val scheme = uri.scheme?.lowercase()
        require(scheme == "http" || scheme == "https") {
            "Server address must use http or https"
        }

        val host = uri.host
        require(!host.isNullOrBlank()) { "Server address must include a host or IP" }

        val port = if (uri.port == -1) 8080 else uri.port
        require(port in 1..65535) { "Server port must be between 1 and 65535" }

        return "$scheme://$host:$port/"
    }
}
