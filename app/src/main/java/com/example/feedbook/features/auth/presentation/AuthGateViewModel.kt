package com.example.feedbook.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feedbook.core.session.SessionManager
import com.example.feedbook.features.push.PushTokenRegistrar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthGateDestination {
    LOGIN,
    HOME
}

data class AuthGateUiState(
    val isLoading: Boolean = true,
    val destination: AuthGateDestination? = null,
    val biometricPromptTrigger: Int = 0
)

class AuthGateViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(AuthGateUiState())
    val state: StateFlow<AuthGateUiState> = _state.asStateFlow()

    init {
        val session = sessionManager.restorePersistedSession()
        PushTokenRegistrar.registerCurrentToken(session?.username)
        _state.value = when {
            session == null -> AuthGateUiState(
                isLoading = false,
                destination = AuthGateDestination.LOGIN
            )

            session.secureLogin -> AuthGateUiState(
                isLoading = false,
                biometricPromptTrigger = 1
            )

            else -> AuthGateUiState(
                isLoading = false,
                destination = AuthGateDestination.HOME
            )
        }
    }

    fun onBiometricSuccess() {
        val session = sessionManager.restorePersistedSession()
        PushTokenRegistrar.registerCurrentToken(session?.username)
        _state.value = AuthGateUiState(
            isLoading = false,
            destination = if (session == null) AuthGateDestination.LOGIN else AuthGateDestination.HOME
        )
    }

    fun onBiometricError() {
        sessionManager.clearInMemorySession()
        _state.value = AuthGateUiState(
            isLoading = false,
            destination = AuthGateDestination.LOGIN
        )
    }

    companion object {
        fun provideFactory(
            sessionManager: SessionManager
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthGateViewModel(sessionManager) as T
            }
        }
    }
}
