package com.dmadunts.samples.infinsample.remote.utils

import android.content.Context
import com.dmadunts.samples.infinsample.remote.api.GitHubApi
import com.dmadunts.samples.infinsample.remote.repositories.implementation.GitHubRepositoryImpl
import com.dmadunts.samples.infinsample.screens.search.SearchViewModel
import com.dmadunts.samples.infinsample.utils.exceptions.NoConnectivityException
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.getKoin
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return sendRequest(apiCall)
    }

    private suspend fun <T : Any> sendRequest(
        apiCall: suspend () -> Response<T>,
    ): Resource<T> {
        val context by lazy { get(Context::class.java) }
        return try {
            val response = apiCall()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success<T>(response.body() as T)
            } else if (response.isSuccessful) {
                Resource.Success<T>("" as T)
            } else {
                Resource.Error(Exception(response.errorBody().toException()))
            }
        } catch (throwable: Throwable) {
            return when (throwable) {
                is NoConnectivityException -> {
                    Resource.Error(
                        NoConnectivityException(context)
                    )
                }
                is HttpException -> {
                    Resource.Error(throwable.response()?.errorBody().toException())
                }
                else -> {
                    Resource.Error(Exception(throwable.message))
                }
            }
        }
    }

    fun switchBaseUrl(baseUrl: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getKoin().get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(GitHubApi::class.java)
        val repo = GitHubRepositoryImpl(api)
        getKoin().declare(retrofit, override = true)
        getKoin().declare(api, override = true)
        getKoin().declare(repo, override = true)
        getKoin().get(SearchViewModel::class).repository = repo
    }
}
