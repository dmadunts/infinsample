package com.dmadunts.samples.infinsample.utils.exceptions

import android.content.Context
import com.dmadunts.samples.infinsample.R
import java.io.IOException

class NoConnectivityException(private val context: Context) : IOException() {
    override fun getLocalizedMessage(): String {
        return context.applicationContext.getString(R.string.NoConnectivityException)
    }

    override val message: String
        get() = context.applicationContext.getString(R.string.NoConnectivityException)

}