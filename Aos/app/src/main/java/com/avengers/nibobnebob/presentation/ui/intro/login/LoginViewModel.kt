package com.avengers.nibobnebob.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.IntroRepository
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

    private val _uiState = MutableStateFlow(CommonRequest())
    val uiState = _uiState.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val autoLogin = MutableStateFlow(false)

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
            dataStoreManager.putAccessToken(token)
            introRepository.loginNaver().onEach {
                when(it){
                    is BaseState.Success -> {
                        dataStoreManager.putAutoLogin(true)
                        dataStoreManager.putAccessToken(it.data.body.accessToken.toString())
                        dataStoreManager.putRefreshToken(it.data.body.refreshToken.toString())

                        _events.emit(LoginEvent.NavigateToMain)
                    }
                    is BaseState.Error -> {
                        _events.emit(LoginEvent.NavigateToDetailSignup)
//                        when(it.statusCode){
//                            401 -> {
//                                Log.d(TAG,"401이 뜰일이 있나..?")
//                            }
//                            404 -> {
//                                _events.emit(LoginEvent.NavigateToDetailSignup)
//                            }
//                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}