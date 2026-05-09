package com.example.feedbook.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.core.session.SessionManager
import com.example.feedbook.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password, errorMessage = null)
    }

    fun submitLogin(onSuccess: () -> Unit) {
        val currentState = _state.value
        if (currentState.username.isBlank() || currentState.password.isBlank() || currentState.isLoading) {
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true, errorMessage = null)

            runCatching {
                loginUseCase(
                    username = currentState.username,
                    password = currentState.password,
                    easyLogin = false
                )
            }.onSuccess { session ->
                sessionManager.updateSession(session)
                _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                onSuccess()
            }.onFailure { throwable ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = throwable.toLoginErrorMessage()
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            loginUseCase: LoginUseCase,
            sessionManager: SessionManager
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(loginUseCase, sessionManager) as T
            }
        }
    }
}

private fun Throwable.toLoginErrorMessage(): String = when (this) {
    is HttpException -> if (code() == 401) {
        "Invalid user or password"
    } else {
        "Unable to sign in right now"
    }
    is IOException -> "Cannot reach login server on localhost:8080"
    else -> message ?: "Unable to sign in right now"
}
