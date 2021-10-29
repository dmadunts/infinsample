package com.dmadunts.samples.infinsample.remote.repositories.interfaces

import com.dmadunts.samples.infinsample.remote.models.AccessToken
import com.dmadunts.samples.infinsample.remote.models.Codes
import com.dmadunts.samples.infinsample.remote.utils.Resource
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): Flow<Resource<AccessToken>>

    suspend fun getVerificationCodes(clientId: String, scope: String): Flow<Resource<Codes>>
}