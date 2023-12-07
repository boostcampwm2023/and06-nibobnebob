package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.GetEmailValidationUseCase
import com.avengers.nibobnebob.presentation.util.ValidationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BasicSignupUiState(
    val emailState: InputState = InputState.Empty,
    val passwordCheckState: InputState = InputState.Empty,
    val isEmailNotEmpty: Boolean = true
)

sealed class BasicSignupEvents{
    data object NavigateToBack : BasicSignupEvents()
    data class NavigateToDetailSignup(
        val provider: String,
        val email: String,
        val password: String,
    ) : BasicSignupEvents()
    data class ShowSnackMessage(val msg: String): BasicSignupEvents()
}

@HiltViewModel
class BasicSignupViewModel @Inject constructor(
    private val getEmailValidationUseCase: GetEmailValidationUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(BasicSignupUiState())
    val uiState: StateFlow<BasicSignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BasicSignupEvents>()
    val events: SharedFlow<BasicSignupEvents> = _events.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordCheck = MutableStateFlow("")
    private val emailValidation = MutableStateFlow(false)
    private val passwordValidation = MutableStateFlow(false)

    val isDataReady = combine(emailValidation, passwordValidation){ emailValidation, passwordValidation ->
        emailValidation && passwordValidation
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init{
        observeEmail()
        observePasswordCheck()
    }

    private fun observeEmail(){
        email.onEach {
            if(ValidationUtil.checkEmail(it)){
                _uiState.update { state ->
                    emailValidation.value = false
                    state.copy(
                        isEmailNotEmpty = it.isNotBlank(),
                        emailState = InputState.Empty
                    )
                }
            } else {
                _uiState.update { state ->
                    emailValidation.value = false
                    state.copy(
                        isEmailNotEmpty = false,
                        emailState = InputState.Error("올바른 이메일 형식이 아닙니다")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observePasswordCheck(){
        passwordCheck.onEach {
            if(it.isNotBlank()){
                if(it == password.value){
                    passwordValidation.value = true
                    _uiState.update { state ->
                        state.copy(
                            passwordCheckState = InputState.Success("비밀번호가 일치합니다.")
                        )
                    }
                } else {
                    passwordValidation.value = false
                    _uiState.update { state ->
                        state.copy(
                            passwordCheckState = InputState.Error("비밀번호가 일치하지 않습니다.")
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkEmail(){
        getEmailValidationUseCase(email.value).onEach {
            when(it){
                is BaseState.Success -> {
                    if(it.data.isExist){
                        emailValidation.value = false
                        _uiState.update { state ->
                            state.copy(
                                emailState = InputState.Error("사용할 수 없는 이메일 입니다")
                            )
                        }
                    } else {
                        emailValidation.value = true
                        _uiState.update { state ->
                            state.copy(
                                emailState = InputState.Success("사용 가능한 이메일 입니다")
                            )
                        }
                    }
                }
                is BaseState.Error -> {
                    emailValidation.value = false
                    _events.emit(BasicSignupEvents.ShowSnackMessage(it.message))
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