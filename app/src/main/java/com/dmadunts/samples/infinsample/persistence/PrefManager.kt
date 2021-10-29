package com.dmadunts.samples.infinsample.persistence

import android.content.Context
import android.content.SharedPreferences

private val INFIN = "prefs"
private val ACCESS_TOKEN = "access_token"
private fun getPrefInstance(context: Context): SharedPreferences {
    return context.getSharedPreferences(INFIN, Context.MODE_PRIVATE)
}

fun Context.getAccessToken(): String? {
    return getPrefInstance(this).getString(ACCESS_TOKEN, null)
}

fun Context.setAccessToken(accessToken: String) {
    getPrefInstance(this)
        .edit()
        .putString(ACCESS_TOKEN, accessToken)
        .commit()
}
fun Context.clearPrefs() {
    getPrefInstance(this).edit().clear().commit()
}