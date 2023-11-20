package com.avengers.nibobnebob.presentation.ui.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.data.repository.LoginRepository
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStoreManager: DataStoreManager,
    private val loginRepository: LoginRepository
) : BaseActivityViewModel(networkManager) {

    sealed class NavigationEvent {
        data object NavigateToMain : NavigationEvent()
        data object NavigateToIntro : NavigationEvent()
    }

    private val TAG = "SplashViewModelDebug"
    private val _events = MutableSharedFlow<NavigationEvent>()
    val events: SharedFlow<NavigationEvent> get() = _events

    fun getAutoLogin() {
        viewModelScope.launch {
            val auto = dataStoreManager.getAutoLogin().first()
            val access = dataStoreManager.getAccessToken().first()
            val refresh = dataStoreManager.getRefreshToken().first()
            Log.d(TAG ,"auto = $auto")
            Log.d(TAG ,"access = $access")
            Log.d(TAG ,"refresh = $refresh")
            dataStoreManager.getAutoLogin().collect { autoLogin ->
                if (autoLogin == true) {
                    _events.emit(NavigationEvent.NavigateToMain)
                } else {
                    _events.emit(NavigationEvent.NavigateToIntro)
                }
            }
        }
    }
}