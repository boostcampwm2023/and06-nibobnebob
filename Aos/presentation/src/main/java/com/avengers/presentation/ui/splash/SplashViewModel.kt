package com.avengers.presentation.ui.splash

import androidx.lifecycle.viewModelScope
import com.avengers.data.config.DataStoreManager
import com.avengers.data.config.NetworkManager
import com.avengers.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiEvent {
    data object NavigateToMain : SplashUiEvent()
    data object NavigateToIntro : SplashUiEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val dataStoreManager: DataStoreManager,
) : BaseActivityViewModel(networkManager) {
    private val _events = MutableSharedFlow<SplashUiEvent>()
    val events: SharedFlow<SplashUiEvent> = _events.asSharedFlow()

    fun getAutoLogin() {
        viewModelScope.launch {
            dataStoreManager.getAutoLogin().collect { autoLogin ->
                dataStoreManager.getAccessToken().collect { accessToken ->
                    if (autoLogin == true && accessToken != "") {
                        _events.emit(SplashUiEvent.NavigateToMain)
                    } else {
                        _events.emit(SplashUiEvent.NavigateToIntro)
                    }
                }
            }
        }
    }
}