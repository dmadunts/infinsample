package com.dmadunts.samples.infinsample.remote.repositories.implementation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dmadunts.samples.infinsample.remote.api.GitHubApi
import com.dmadunts.samples.infinsample.remote.models.Item
import com.dmadunts.samples.infinsample.remote.pagingsources.SearchPagingSource
import com.dmadunts.samples.infinsample.remote.repositories.interfaces.GitHubRepository
import com.dmadunts.samples.infinsample.remote.utils.NetworkManager
import com.dmadunts.samples.infinsample.remote.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GitHubRepositoryImpl(private val api: GitHubApi) : GitHubRepository {
    override suspend fun getAccessToken(clientId: String, clientSecret: String, code: String) =
        flow {
            emit(Resource.Loading)
            emit(NetworkManager.safeApiCall { api.getAccessToken(clientId, clientSecret, code) })
        }

    override suspend fun getVerificationCodes(clientId: String, scope: String) =
        flow {
            emit(Resource.Loading)
            emit(NetworkManager.safeApiCall { api.getVerificationCodes(clientId, scope) })
        }

    fun searchRepos(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(50),
            pagingSourceFactory = { SearchPagingSource(query, api) }
        ).flow
    }

}
