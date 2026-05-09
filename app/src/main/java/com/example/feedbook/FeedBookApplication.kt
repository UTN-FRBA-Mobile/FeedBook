package com.example.feedbook

import android.app.Application
import com.example.feedbook.core.di.AppContainer

class FeedBookApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
