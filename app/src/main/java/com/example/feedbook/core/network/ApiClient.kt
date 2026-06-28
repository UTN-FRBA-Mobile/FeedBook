package com.example.feedbook.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Proxy
import javax.net.SocketFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun createOkHttpClient(
        useSystemProxy: Boolean = true,
        socketFactory: SocketFactory? = null
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()
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
