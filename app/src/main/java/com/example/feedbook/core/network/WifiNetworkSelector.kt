package com.example.feedbook.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.net.SocketFactory

class WifiNetworkSelector(context: Context) {
    private val connectivityManager = context.applicationContext
        .getSystemService(ConnectivityManager::class.java)

    fun socketFactoryForWifi(): SocketFactory? {
        val manager = connectivityManager ?: return null
        return manager.allNetworks
            .firstOrNull { network ->
                manager.getNetworkCapabilities(network)
                    ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
            }
            ?.socketFactory
    }
}
