package com.example.feedbook.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feedbook.core.network.BackendServerConfig
import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.core.session.SessionManager
import com.example.feedbook.features.auth.domain.usecase.LoginUseCase
import com.example.feedbook.features.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val secureLoginEnabled: Boolean = false,
    val isRegisterMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val biometricPromptTrigger: Int = 0,
    val serverOrigin: String = "",
    val serverOriginDraft: String = "",
    val serverConfigError: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
    private val backendServerConfig: BackendServerConfig
) : ViewModel() {
    private val initialServerOrigin = backendServerConfig.getOrigin()
    private val _state = MutableStateFlow(
        LoginUiState(
            serverOrigin = initialServerOrigin,
            serverOriginDraft = initialServerOrigin
        )
    )
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username, errorMessage = null, successMessage = null)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password, errorMessage = null, successMessage = null)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword, errorMessage = null, successMessage = null)
    }

    fun updateSecureLoginEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(secureLoginEnabled = enabled, errorMessage = null, successMessage = null)
    }

    fun updateServerOriginDraft(serverOrigin: String) {
        _state.value = _state.value.copy(serverOriginDraft = serverOrigin, serverConfigError = null)
    }

    fun saveServerOrigin(): Boolean {
        val currentState = _state.value
        return runCatching {
            backendServerConfig.setOrigin(currentState.serverOriginDraft)
        }.onSuccess { normalizedOrigin ->
            NetworkModule.updateBackendOrigin(normalizedOrigin)
            _state.value = currentState.copy(
                serverOrigin = normalizedOrigin,
                serverOriginDraft = normalizedOrigin,
                serverConfigError = null,
                errorMessage = null,
                successMessage = "Server set to $normalizedOrigin"
            )
        }.onFailure { error ->
            _state.value = currentState.copy(
                serverConfigError = error.message ?: "Invalid server address"
            )
        }.isSuccess
    }

    fun resetServerOrigin() {
        val defaultOrigin = backendServerConfig.resetOrigin()
        NetworkModule.updateBackendOrigin(defaultOrigin)
        _state.value = _state.value.copy(
            serverOrigin = defaultOrigin,
            serverOriginDraft = defaultOrigin,
            serverConfigError = null,
            errorMessage = null,
            successMessage = "Server reset to $defaultOrigin"
        )
    }

    fun showRegisterMode() {
        _state.value = _state.value.copy(isRegisterMode = true, errorMessage = null, successMessage = null)
    }

    fun showLoginMode() {
        _state.value = _state.value.copy(isRegisterMode = false, confirmPassword = "", errorMessage = null)
    }

    fun submitLogin(onSuccess: () -> Unit) {
        val currentState = _state.value
        if (currentState.username.isBlank() || currentState.password.isBlank() || currentState.isLoading) {
            return
        }

        if (currentState.secureLoginEnabled) {
            _state.value = currentState.copy(
                errorMessage = null,
                successMessage = null,
                biometricPromptTrigger = currentState.biometricPromptTrigger + 1
            )
            return
        }

        performLogin(secureLogin = false, onSuccess = onSuccess)
    }

    fun submitRegister(onSuccess: () -> Unit) {
        val currentState = _state.value
        if (currentState.isLoading) {
            return
        }
        val validationError = currentState.registrationValidationError()
        if (validationError != null) {
            _state.value = currentState.copy(errorMessage = validationError, successMessage = null)
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true, errorMessage = null)

            runCatching {
                registerUseCase(
                    username = currentState.username,
                    password = currentState.password,
                    secureLogin = currentState.secureLoginEnabled
                )
            }.onSuccess {
                _state.value = _state.value.copy(
                    password = "",
                    confirmPassword = "",
                    isRegisterMode = false,
                    isLoading = false,
                    errorMessage = null,
                    successMessage = "Account created. Sign in to continue"
                )
                onSuccess()
            }.onFailure { throwable ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = throwable.toAuthErrorMessage(registerMode = true),
                    successMessage = null
                )
            }
        }
    }

    fun onSecureLoginAuthenticationSucceeded(onSuccess: () -> Unit) {
        performLogin(secureLogin = true, onSuccess = onSuccess)
    }

    fun onSecureLoginAuthenticationError(message: String) {
        _state.value = _state.value.copy(errorMessage = message)
    }

    private fun performLogin(
        secureLogin: Boolean,
        onSuccess: () -> Unit
    ) {
        val currentState = _state.value
        if (currentState.username.isBlank() || currentState.password.isBlank() || currentState.isLoading) {
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true, errorMessage = null, successMessage = null)

            runCatching {
                loginUseCase(
                    username = currentState.username,
                    password = currentState.password,
                    secureLogin = secureLogin
                )
            }.onSuccess { session ->
                sessionManager.updateSession(session)
                _state.value = _state.value.copy(isLoading = false, errorMessage = null)
                onSuccess()
            }.onFailure { throwable ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = throwable.toAuthErrorMessage(registerMode = false),
                    successMessage = null
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            loginUseCase: LoginUseCase,
            registerUseCase: RegisterUseCase,
            sessionManager: SessionManager,
            backendServerConfig: BackendServerConfig
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(
                    loginUseCase,
                    registerUseCase,
                    sessionManager,
                    backendServerConfig
                ) as T
            }
        }
    }
}

private fun LoginUiState.registrationValidationError(): String? = when {
    username.isBlank() || password.isBlank() || confirmPassword.isBlank() -> "Complete all fields"
    password.length < 4 -> "Password must be at least 4 characters"
    password != confirmPassword -> "Passwords do not match"
    else -> null
}

private fun Throwable.toAuthErrorMessage(registerMode: Boolean): String = when (this) {
    is HttpException -> if (code() == 401) {
        "Invalid user or password"
    } else if (code() == 409) {
        "An account with that email already exists"
    } else {
        if (registerMode) "Unable to create account right now" else "Unable to sign in right now"
    }
    is IOException -> "Cannot reach auth server. Check the server IP and port."
    else -> message ?: if (registerMode) "Unable to create account right now" else "Unable to sign in right now"
}
