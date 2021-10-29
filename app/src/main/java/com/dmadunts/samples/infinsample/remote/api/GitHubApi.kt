package com.dmadunts.samples.infinsample.remote.api

import com.dmadunts.samples.infinsample.remote.models.AccessToken
import com.dmadunts.samples.infinsample.remote.models.Codes
import com.dmadunts.samples.infinsample.remote.models.Repository
import retrofit2.Response
import retrofit2.http.*

interface GitHubApi {
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ): Response<AccessToken>

    @POST("login/device/code")
    @FormUrlEncoded
    suspend fun getVerificationCodes(
        @Field("client_id") clientId: String,
        @Field("scope") code: String,
    ): Response<Codes>

    @GET("search/repositories")
    suspend fun getRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<Repository>
}