package com.dmadunts.samples.infinsample.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dmadunts.samples.infinsample.remote.models.Item
import com.dmadunts.samples.infinsample.remote.repositories.implementation.GitHubRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class SearchViewModel : ViewModel() {
    var repository: GitHubRepositoryImpl = get(GitHubRepositoryImpl::class.java)
    private val _searchReposResponse = MutableLiveData<PagingData<Item>>()
    val searchReposResponse: LiveData<PagingData<Item>>
        get() = _searchReposResponse

    fun searchRepos(query: String) = viewModelScope.launch {
        repository.searchRepos(query).cachedIn(viewModelScope).collectLatest {
            _searchReposResponse.value = it
        }
    }
}