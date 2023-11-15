package com.avengers.nibobnebob.presentation.ui.intro.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val TAG = "LoginViewModelDebug"

    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(CommonRequest())
    val uiState = _uiState.asStateFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val token = MutableStateFlow("")
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

    fun postCommonLogin(){
        Log.d(TAG,_uiState.value.toString())
        Log.d(TAG,autoLogin.value.toString())
        viewModelScope.launch {
            //TODO : flow
            //repository -> 통신 -> flow로
        }
    }

    fun postNaverLogin(){
        viewModelScope.launch {
            //네이버 통신 받아온것을 이제 서버와 통신 로그인 이벤트를 성공시 -> 메인 이벤트를 eventflow로 진행?
            //통신 -> 토큰 사용 -> 서버와의 네이버 로그인 진행 예정
        }
    }

    sealed class LoginEvent {
        data object LoginSuccess : LoginEvent()
        data object LoginFailure : LoginEvent()
    }


}

/*
interface ApiService {
    @POST("login")
    suspend fun postLogin(@Body info: Info): Flow<로그인정보>
}

    class 로그인repository(private val apiService: 인포 서비스) {
        apiService.postLogin(info)
        .map { response ->
            // 여기서 음 그 뭐야 여기서 이제 eventflow처리?
            response.body() ?: throw SomeException("Response body is null")
        }
        .onStart {
            // 로딩 상태중에 로딩이란걸 보여주는 처리를 한다면?
        }
        .catch { e ->
            // 에러 핼들링
            throw when (e) {
                is HttpException -> AnotherException("HTTP Error: ${e.code()}")
                else -> e
            }
        }.flowOn(Dispatchers.IO)
    }


 */