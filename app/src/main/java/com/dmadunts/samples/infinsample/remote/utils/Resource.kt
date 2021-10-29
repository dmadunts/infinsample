package com.dmadunts.samples.infinsample.remote.utils

import okhttp3.ResponseBody
import java.io.IOException

sealed class Resource<out T : Any> {
    object Loading : Resource<Nothing>()
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
}

fun ResponseBody?.toException() = IOException(this?.string())