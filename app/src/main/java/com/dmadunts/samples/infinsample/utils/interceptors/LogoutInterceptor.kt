package com.dmadunts.samples.infinsample.utils.interceptors

import android.content.Context
import android.content.Intent
import com.dmadunts.samples.infinsample.MainActivity
import com.dmadunts.samples.infinsample.persistence.clearPrefs
import com.dmadunts.samples.infinsample.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONException
import org.koin.java.KoinJavaComponent


class LogoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val globJson = response.body!!.string()
        try {
            if (response.code == 401) {
                val context = KoinJavaComponent.get(Context::class.java)
                context.clearPrefs()
                val intent = Intent(context, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(Constants.ACTION_LOGOUT, true)
                context.startActivity(intent)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body(ResponseBody.create(response.body!!.contentType(), globJson)).build()
    }
}