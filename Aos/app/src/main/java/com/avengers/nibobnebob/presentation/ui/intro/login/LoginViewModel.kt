package com.avengers.nibobnebob.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.IntroRepository
import com.avengers.nibobnebob.presentation.ui.intro.signup.InputState
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
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


data class LoginUiState(
    val commonLoginState: InputState = InputState.Empty
)

sealed class LoginEvent {
    data object NavigateToMain : LoginEvent()
    data object NavigateToBasicSignup : LoginEvent()
    data object NavigateToDetailSignup : LoginEvent()
    data object NavigateToDialog : LoginEvent()
    data class ShowSnackMessage(
        val msg: String
    ) : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val autoLogin = MutableStateFlow(false)
    val naverEmail = MutableStateFlow("")

    init {
        observeEmail()
        observePassword()
    }

    private fun observeEmail() {
        email.onEach {
            _uiState.update { state ->
                state.copy(
                    commonLoginState = InputState.Empty
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observePassword() {
        password.onEach {
            _uiState.update { state ->
                state.copy(
                    commonLoginState = InputState.Empty
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setAutoLogin(state: Boolean) {
        autoLogin.value = state
    }

    fun loginCommon() {
        introRepository.loginBasic(
            email.value, password.value
        ).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    loginSuccess(
                        autoLogin.value,
                        state.data.accessToken.toString(),
                        state.data.refreshToken.toString()
                    )
                }

                is BaseState.Error -> {
                    when (state.statusCode) {
                        StatusCode.EXCEPTION -> _events.emit(LoginEvent.ShowSnackMessage(state.message))
                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    commonLoginState = InputState.Error("아이디/비밀번호가 일치하지 않습니다.")
                                )
                            }
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loginNaver(token: String) {
        viewModelScope.launch {
            introRepository.loginNaver(token).onEach { state ->
                when (state) {
                    is BaseState.Success -> {
                        loginSuccess(
                            true,
                            state.data.accessToken.toString(),
                            state.data.refreshToken.toString()
                        )
                    }

                    is BaseState.Error -> {
                        when (state.statusCode) {
                            StatusCode.ERROR_NONE -> _events.emit(LoginEvent.NavigateToDetailSignup)
                            else -> _events.emit(LoginEvent.ShowSnackMessage(ERROR_MSG))
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loginSuccess(autoLogin: Boolean, access: String, refresh: String) {
        viewModelScope.launch {
            dataStoreManager.putAutoLogin(autoLogin)
            dataStoreManager.putAccessToken(access)
            dataStoreManager.putRefreshToken(refresh)
            _events.emit(LoginEvent.NavigateToMain)
        }
    }

    fun navigateToBasicSignup() {
        viewModelScope.launch {
            _events.emit(LoginEvent.NavigateToBasicSignup)
        }
    }

}