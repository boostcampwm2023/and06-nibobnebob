package com.avengers.nibobnebob.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.NetworkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseActivityViewModel (
    private val networkManager: NetworkManager
): ViewModel() {
    private val _networkState = MutableStateFlow(NetWorkState.NONE)
    val networkState:StateFlow<NetWorkState> = _networkState.asStateFlow()

    init{
        networkManager.startNetwork()
        checkNetworkState()
    }

    private fun checkNetworkState(){
        viewModelScope.launch {

            networkManager.isNetworkConnected.collectLatest{
                if(it) _networkState.update{ NetWorkState.NETWORK_CONNECTED }
                else _networkState.update { NetWorkState.NETWORK_DISCONNECTED }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkManager.endNetwork()
    }
}

enum class NetWorkState{
NONE,
NETWORK_CONNECTED,
NETWORK_DISCONNECTED
}
