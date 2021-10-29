package com.dmadunts.samples.infinsample.utils.interceptors

import android.content.Context
import com.dmadunts.samples.infinsample.utils.NetworkMonitor
import com.dmadunts.samples.infinsample.utils.exceptions.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder();
        if (NetworkMonitor.isNetworkConnected)
            return chain.proceed(builder.build())
        else
            throw NoConnectivityException(context)
    }
}