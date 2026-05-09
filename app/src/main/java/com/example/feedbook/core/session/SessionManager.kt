package com.example.feedbook.core.session

import com.example.feedbook.features.auth.domain.model.AuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {
    private val _session = MutableStateFlow<AuthSession?>(null)
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    fun updateSession(session: AuthSession) {
        _session.value = session
    }

    fun clearSession() {
        _session.value = null
    }
}
