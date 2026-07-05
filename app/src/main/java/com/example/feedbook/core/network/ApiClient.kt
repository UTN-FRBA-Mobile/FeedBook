package com.example.feedbook.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import javax.net.SocketFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun createOkHttpClient(
        useSystemProxy: Boolean = true,
        socketFactory: SocketFactory? = null,
        authTokenProvider: (() -> String?)? = null
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = authTokenProvider?.invoke()?.takeIf { it.isNotBlank() }
                val request = if (token == null) {
                    chain.request()
                } else {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)

        if (!useSystemProxy) {
            builder.proxy(Proxy.NO_PROXY)
        }

        if (socketFactory != null) {
            builder.socketFactory(socketFactory)
        }

        return builder.build()
    }
}
