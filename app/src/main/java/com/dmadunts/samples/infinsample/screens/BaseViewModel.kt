package com.dmadunts.samples.infinsample.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmadunts.samples.infinsample.remote.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class BaseViewModel() : ViewModel() {
    fun <T> handle(flow: Flow<T>, listener: MutableLiveData<T>) =
        viewModelScope.launch {
            flow.onEach {
                listener.value = it
            }.launchIn(viewModelScope)
        }

    fun <T> handleSingle(flow: Flow<T>, listener: MutableLiveData<Event<T>>) =
        viewModelScope.launch {
            flow.onEach {
                listener.value = Event(it)
            }.launchIn(viewModelScope)
        }
}