package com.example.feedbook.core.network

import android.content.Context
import com.example.feedbook.BuildConfig

class BackendServerConfig(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        "backend_server_config",
        Context.MODE_PRIVATE
    )

    val defaultOrigin: String = BackendUrls.origin(BuildConfig.BACKEND_ORIGIN)

    fun getOrigin(): String = preferences.getString(KEY_ORIGIN, null) ?: defaultOrigin

    fun setOrigin(rawOrigin: String): String {
        val normalizedOrigin = BackendUrls.normalizeServerOrigin(rawOrigin)
        preferences.edit().putString(KEY_ORIGIN, normalizedOrigin).apply()
        return normalizedOrigin
    }

    fun resetOrigin(): String {
        preferences.edit().remove(KEY_ORIGIN).apply()
        return defaultOrigin
    }

    companion object {
        private const val KEY_ORIGIN = "origin"
    }
}
