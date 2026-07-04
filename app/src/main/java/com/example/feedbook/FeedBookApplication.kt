package com.example.feedbook

import android.app.Application
import com.example.feedbook.core.di.AppContainer
import com.example.feedbook.core.network.NetworkModule
import com.example.feedbook.features.push.PushTokenRegistrar

class FeedBookApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        NetworkModule.initialize(this)
        PushTokenRegistrar.registerCurrentToken(container.sessionManager.session.value?.username)
    }
}
