package com.dmadunts.samples.infinsample

import android.app.Application
import com.dmadunts.samples.infinsample.di.appModule
import com.dmadunts.samples.infinsample.di.networkModule
import com.dmadunts.samples.infinsample.di.viewModelsModule
import com.dmadunts.samples.infinsample.utils.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationLoader : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    viewModelsModule,
                    networkModule,
                    appModule,
                )
            )
        }
        NetworkMonitor.startNetworkCallback(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor.stopNetworkCallback(this)
    }
}