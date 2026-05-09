package com.example.feedbook.core.session

import android.content.Context

class SessionStorage(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun readToken(): String? = preferences.getString(KEY_TOKEN, null)

    fun writeToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun clear() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "feedbook_session"
        const val KEY_TOKEN = "jwt_token"
    }
}
