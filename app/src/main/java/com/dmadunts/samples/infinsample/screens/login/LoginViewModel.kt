package com.dmadunts.samples.infinsample.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmadunts.samples.infinsample.remote.models.AccessToken
import com.dmadunts.samples.infinsample.remote.models.Codes
import com.dmadunts.samples.infinsample.remote.repositories.implementation.GitHubRepositoryImpl
import com.dmadunts.samples.infinsample.remote.utils.Event
import com.dmadunts.samples.infinsample.remote.utils.Resource
import com.dmadunts.samples.infinsample.screens.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: GitHubRepositoryImpl) : BaseViewModel() {
    private val _accessToken = MutableLiveData<Event<Resource<AccessToken>>>()
    val accessToken: LiveData<Event<Resource<AccessToken>>> = _accessToken
    fun getAccessToken(clientId: String, clientSecret: String, code: String) {
        viewModelScope.launch {
            handleSingle(repository.getAccessToken(clientId, clientSecret, code), _accessToken)
        }
    }
}