package com.avengers.nibobnebob.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    sealed class NavigationEvent {
        data object NavigateToMain : NavigationEvent()
        data object NavigateToIntro : NavigationEvent()
    }

    private val _eventFlow = MutableSharedFlow<NavigationEvent>()
    val eventFlow: SharedFlow<NavigationEvent> get() = _eventFlow

    val autoLogin = MutableStateFlow(false)
    val accessToken = MutableStateFlow("")
    val refreshToken = MutableStateFlow("")

    fun getAutoLogin() {
        viewModelScope.launch {
//            autoLogin.value = IntroRepository.getBoolean(AUTOLOGIN)
            if (autoLogin.value) {
//                accessToken.value = IntroRepository.getString(ACCESSTOKEN)
//                refreshToken.value = IntroRepository.getString(refreshToken)
            }

            //accessToken 유효할때
            if (accessTokenIsValid()) {
                moveToMainActivity()
            } else {
                reissueAccessToken()
            }
        }
    }

    private fun accessTokenIsValid(): Boolean {
        //TODO : 테스트용 TRUE,FALSE이지만 여기서 서버와 통신 진행해야함
        return true
    }

    private fun reissueAccessToken() {
        //TODO : refreshToken으로 통신
        if (accessTokenIsValid()) {
            moveToMainActivity()
        } else {
            moveToLoginActivity()
        }
    }


    private fun emitNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    private fun moveToMainActivity() {
        emitNavigationEvent(NavigationEvent.NavigateToMain)
    }

    private fun moveToLoginActivity() {
        emitNavigationEvent(NavigationEvent.NavigateToIntro)
    }
}