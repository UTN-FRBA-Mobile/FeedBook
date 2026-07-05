package com.example.feedbook.core.session

import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.features.auth.domain.model.AuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

class SessionManager(
    private val sessionStorage: SessionStorage
) {
    private val _session = MutableStateFlow(loadValidSession())
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    fun updateSession(session: AuthSession) {
        sessionStorage.writeToken(session.token)
        NetworkModule.updateAuthToken(session.token)
        _session.value = session
    }

    fun clearSession() {
        sessionStorage.clear()
        NetworkModule.updateAuthToken(null)
        _session.value = null
    }

    fun clearInMemorySession() {
        NetworkModule.updateAuthToken(null)
        _session.value = null
    }

    fun restorePersistedSession(): AuthSession? {
        val session = loadValidSession()
        NetworkModule.updateAuthToken(session?.token)
        _session.value = session
        return session
    }

    private fun loadValidSession(): AuthSession? {
        val token = sessionStorage.readToken() ?: return null
        val session = JwtSessionParser.parse(token) ?: run {
            sessionStorage.clear()
            NetworkModule.updateAuthToken(null)
            return null
        }

        if (session.expiresAtEpochSeconds <= Instant.now().epochSecond) {
            sessionStorage.clear()
            NetworkModule.updateAuthToken(null)
            return null
        }

        return session
    }
}
