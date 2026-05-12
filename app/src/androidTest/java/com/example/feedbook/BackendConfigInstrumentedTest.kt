package com.example.feedbook

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.feedbook.core.network.BackendUrls
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackendConfigInstrumentedTest {
    @Test
    fun backendOriginBuildConfigResolvesApiUrl() {
        val apiBaseUrl = BackendUrls.apiBaseUrl(BuildConfig.BACKEND_ORIGIN)
        assertTrue(apiBaseUrl.endsWith("/api/"))
    }
}
