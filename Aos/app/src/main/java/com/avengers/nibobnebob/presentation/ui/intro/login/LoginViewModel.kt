package com.avengers.nibobnebob.presentation.ui.intro.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.StatusCode
import com.avengers.nibobnebob.data.repository.IntroRepository
import com.avengers.nibobnebob.presentation.ui.intro.login.model.UiLoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginEvent {
    data object NavigateToMain : LoginEvent()
    data object NavigateToDetailSignup : LoginEvent()
    data object NavigateToDialog : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val TAG = "LoginViewModelDebug"

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(UiLoginData())
    val uiState = _uiState.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val autoLogin = MutableStateFlow(false)
    val naverEmail = MutableStateFlow("")

    init {
        observeEmail()
        observePassword()
    }

    private fun observeEmail(){
        email.onEach { newEmail ->
            _uiState.update { state ->
                state.copy(email = newEmail)
            }
        }.launchIn(viewModelScope)
    }

    private fun observePassword() {
        password.onEach { newPassword ->
            _uiState.update { state ->
                state.copy(password = newPassword)
            }
        }.launchIn(viewModelScope)
    }

    fun setAutoLogin(newState: Boolean) {
        autoLogin.value = newState
    }

    fun loginCommon(){
        //TODO : 일반로그인
    }

    fun loginNaver(token : String){
        viewModelScope.launch {
            introRepository.loginNaver(token).onEach { state ->
                when(state){
                    is BaseState.Success -> {
                        dataStoreManager.putAutoLogin(true)
                        dataStoreManager.putAccessToken(state.data.body.accessToken.toString())
                        dataStoreManager.putRefreshToken(state.data.body.refreshToken.toString())
                        _events.emit(LoginEvent.NavigateToMain)
                    }
                    is BaseState.Error -> {
                        when(state.statusCode){
                            StatusCode.ERROR_AUTH-> {Log.d(TAG,"토큰 오류")}
                            StatusCode.ERROR_NONE ->{ _events.emit(LoginEvent.NavigateToDetailSignup)}
                            else ->{
                                Log.d(TAG,"오류 Exception")
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}