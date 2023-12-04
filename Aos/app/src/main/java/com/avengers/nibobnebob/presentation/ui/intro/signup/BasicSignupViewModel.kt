package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BasicSignupUiState(
    val emailState: InputState = InputState.Empty,
    val passwordCheckState: InputState = InputState.Empty
)

sealed class BasicSignupEvents{
    data object NavigateToBack : BasicSignupEvents()
    data class NavigateToDetailSignup(
        val provider: String,
        val email: String,
        val password: String,
    ) : BasicSignupEvents()
}

@HiltViewModel
class BasicSignupViewModel @Inject constructor() : ViewModel(){

    private val _uiState = MutableStateFlow(BasicSignupUiState())
    val uiState: StateFlow<BasicSignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BasicSignupEvents>()
    val events: SharedFlow<BasicSignupEvents> = _events.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordCheck = MutableStateFlow("")

    init{
        observePasswordCheck()
    }

    private fun observePasswordCheck(){
        passwordCheck.onEach {
            if(it.isNotBlank()){
                if(it == password.value){
                    _uiState.update { state ->
                        state.copy(
                            passwordCheckState = InputState.Success("비밀번호가 일치합니다.")
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            passwordCheckState = InputState.Error("비밀번호가 일치하지 않습니다.")
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(BasicSignupEvents.NavigateToBack)
        }
    }

    fun navigateToDetailSignup(){
        viewModelScope.launch {
            _events.emit(BasicSignupEvents.NavigateToDetailSignup(
                provider = "site",
                email = email.value,
                password = password.value,
            ))
        }
    }

}