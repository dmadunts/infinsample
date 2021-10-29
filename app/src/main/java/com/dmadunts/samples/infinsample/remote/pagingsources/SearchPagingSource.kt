package com.dmadunts.samples.infinsample.remote.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dmadunts.samples.infinsample.remote.api.GitHubApi
import com.dmadunts.samples.infinsample.remote.models.Item
import com.dmadunts.samples.infinsample.remote.utils.NetworkManager
import com.dmadunts.samples.infinsample.remote.utils.Resource
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val query: String,
    private val api: GitHubApi
) : PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response =
                NetworkManager.safeApiCall { api.getRepositories(query, position, params.loadSize) }
            when (response) {
                is Resource.Success -> {
                    val repos = response.data.items
                    LoadResult.Page(
                        data = repos,
                        prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                        nextKey = if (response.data.items.isNullOrEmpty()) null else position + 1
                    )
                }
                is Resource.Error -> {
                    LoadResult.Error(Exception(response.exception.message))
                }
                else -> {
                    LoadResult.Error(Exception("Unhandled exception"))
                }
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

