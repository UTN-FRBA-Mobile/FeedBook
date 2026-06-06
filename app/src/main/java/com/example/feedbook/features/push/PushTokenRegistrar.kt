package com.example.feedbook.features.push

import android.util.Log
import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.core.network.RegisterPushTokenRequestDto
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object PushTokenRegistrar {
    private const val TAG = "PushTokenRegistrar"

    fun registerCurrentToken() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val token = FirebaseMessaging.getInstance().token.await()
                registerToken(token)
            }.onFailure { error ->
                Log.w(TAG, "Unable to register current FCM token", error)
            }
        }
    }

    fun registerToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                NetworkModule.apiService.registerPushToken(
                    RegisterPushTokenRequestDto(token = token)
                )
            }.onFailure { error ->
                Log.w(TAG, "Unable to register refreshed FCM token", error)
            }
        }
    }
}
