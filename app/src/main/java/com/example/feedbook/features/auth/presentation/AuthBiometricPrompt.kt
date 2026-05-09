package com.example.feedbook.features.auth.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

private const val AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

@Composable
fun AuthBiometricPrompt(
    trigger: Int,
    title: String,
    subtitle: String,
    description: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findFragmentActivity() }
    val executor = remember(activity) { ContextCompat.getMainExecutor(activity) }

    LaunchedEffect(trigger) {
        if (trigger == 0) {
            return@LaunchedEffect
        }

        val biometricManager = BiometricManager.from(activity)
        when (biometricManager.canAuthenticate(AUTHENTICATORS)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val prompt = BiometricPrompt(
                    activity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            onSuccess()
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            onError(errString.toString())
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setAllowedAuthenticators(AUTHENTICATORS)
                    .build()

                prompt.authenticate(promptInfo)
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                onError("Set up fingerprint or device credential to use secure login")
            }

            else -> {
                onError("Biometric or device credential authentication is not available")
            }
        }
    }
}

private fun Context.findFragmentActivity(): FragmentActivity {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is FragmentActivity) {
            return current
        }
        current = current.baseContext
    }
    error("FragmentActivity context required")
}
