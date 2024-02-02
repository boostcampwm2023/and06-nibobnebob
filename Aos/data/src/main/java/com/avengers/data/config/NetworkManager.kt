package com.avengers.data.config

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NetworkManager @Inject constructor(private val connectivityManager: ConnectivityManager) {

    private val _isNetworkConnected = MutableStateFlow(false)
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

// 한번 더 확인하기!!
//    private fun isInternetOn() : Boolean{
//        val network = connectivityManager.activeNetwork
//        val connection = connectivityManager.getNetworkCapabilities(network)
//
//        return connection != null &&(
//                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
//                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
//    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isNetworkConnected.value = true
        }

        override fun onLost(network: Network) {
            _isNetworkConnected.value = false
        }
    }

    fun startNetwork() {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun endNetwork() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}