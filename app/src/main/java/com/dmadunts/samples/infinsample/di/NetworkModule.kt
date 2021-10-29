package com.dmadunts.samples.infinsample.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dmadunts.samples.infinsample.BuildConfig
import com.dmadunts.samples.infinsample.persistence.getAccessToken
import com.dmadunts.samples.infinsample.remote.api.GitHubApi
import com.dmadunts.samples.infinsample.utils.Constants
import com.dmadunts.samples.infinsample.utils.interceptors.ConnectivityInterceptor
import com.dmadunts.samples.infinsample.utils.interceptors.LogoutInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<GitHubApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(GitHubApi::class.java)
    }

    single<Retrofit> {
        val token = get(Context::class.java).getAccessToken()
        Retrofit.Builder()
            .baseUrl(if (token == null) Constants.BASE_URL else Constants.API_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ConnectivityInterceptor(get()))
            .addInterceptor(LogoutInterceptor())
            .addInterceptor { chain ->
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Accept", "application/json")
                    request.addHeader("X-Requested-With", "XMLHttpRequest")
                    return@addInterceptor chain.proceed(request.build())
                } catch (e: Throwable) {
                    throw e
                }
            }
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(ChuckerInterceptor(get()))
        }
        clientBuilder.build()
    }

}