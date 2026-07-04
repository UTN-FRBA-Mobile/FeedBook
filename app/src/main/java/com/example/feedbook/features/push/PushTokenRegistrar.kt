package com.example.feedbook.features.push

import android.util.Log
import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.core.network.RegisterPushTokenRequestDto
import com.example.feedbook.core.network.UnlinkPushTokenRequestDto
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object PushTokenRegistrar {
    private const val TAG = "PushTokenRegistrar"

    fun registerCurrentToken(username: String? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val token = FirebaseMessaging.getInstance().token.await()
                registerToken(token, username)
            }.onFailure { error ->
                Log.w(TAG, "Unable to register current FCM token", error)
            }
        }
    }

    fun registerToken(token: String, username: String? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                NetworkModule.apiService.registerPushToken(
                    RegisterPushTokenRequestDto(token = token, username = username)
                )
            }.onFailure { error ->
                Log.w(TAG, "Unable to register refreshed FCM token", error)
            }
        }
    }

    fun unlinkCurrentToken() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val token = FirebaseMessaging.getInstance().token.await()
                NetworkModule.apiService.unlinkPushToken(UnlinkPushTokenRequestDto(token = token))
            }.onFailure { error ->
                Log.w(TAG, "Unable to unlink current FCM token", error)
            }
        }
    }
}
