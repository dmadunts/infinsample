package com.dmadunts.samples.infinsample.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import kotlin.properties.Delegates

object NetworkMonitor {
    fun startNetworkCallback(application: Application) {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(networkCallback)
        } else {
            cm.registerNetworkCallback(
                builder.build(), networkCallback
            )
        }
    }

    fun stopNetworkCallback(application: Application) {
        val cm: ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isNetworkConnected = true
        }

        override fun onLost(network: Network) {
            isNetworkConnected = false
        }
    }

    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.i("Network connectivity", "$newValue")
    }
}