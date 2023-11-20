package com.avengers.nibobnebob.presentation.ui.intro.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.repository.IntroRepository
import com.avengers.nibobnebob.data.repository.ValidationRepository
import com.avengers.nibobnebob.presentation.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailSignupUiState(
    val isNickNotEmpty: Boolean = false,
    val nickState: InputState = InputState.Empty,
    val birthState: InputState = InputState.Empty,
)

sealed class InputState {
    data object Empty : InputState()
    data class Success(val msg: String) : InputState()
    data class Error(val msg: String) : InputState()
}

sealed class DetailSignupEvents {
    data object NavigateToBack : DetailSignupEvents()
    data object NavigateToMainActivity : DetailSignupEvents()
    data class ShowToastMessage(val msg: String): DetailSignupEvents()
}

@HiltViewModel
class DetailSignupViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val validationRepository: ValidationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailSignupUiState())
    val uiState: StateFlow<DetailSignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DetailSignupEvents>()
    val events: SharedFlow<DetailSignupEvents> = _events.asSharedFlow()

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val provider = MutableStateFlow("")

    val nick = MutableStateFlow("")
    private val nickValidation = MutableStateFlow(false)
    private val isMale = MutableStateFlow(true)
    val birth = MutableStateFlow("")
    val location = MutableStateFlow("")

    val isDataReady = combine(nick, birth, location, nickValidation) { nick, birth, location, nickValidation->
        nick.isNotBlank() && birth.isNotBlank() && location.isNotBlank() &&
                nickValidation
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        observeNick()
        observeBirth()
    }

    private fun observeNick() {

        nick.onEach {
            _uiState.update { state ->
                state.copy(
                    isNickNotEmpty = it.isNotBlank(),
                    nickState = InputState.Empty
                )
            }
        }.onEach {

        }.launchIn(viewModelScope)
    }


    private fun observeBirth() {
        birth.onEach {
            if (Validation.checkBirth(it) || it.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        birthState = InputState.Empty
                    )
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        birthState = InputState.Error("올바른 날짜 형식이 아닙니다")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkNickDuplication() {
        validationRepository.nickValidation(nick.value).onEach {

            when(it){
                is ApiState.Success -> {
                    if(it.data.isExist){
                        nickValidation.value = false
                        _uiState.update { state ->
                            state.copy(
                                nickState = InputState.Error("이미 사용중인 닉네임 입니다")
                            )
                        }
                    } else {
                        nickValidation.value = true
                        _uiState.update { state ->
                            state.copy(
                                nickState = InputState.Success("사용 가능한 닉네임 입니다")
                            )
                        }
                    }
                }
                is ApiState.Error -> {
                    _events.emit(DetailSignupEvents.ShowToastMessage(it.message))
                }
                is ApiState.Exception -> {
                    _events.emit(DetailSignupEvents.ShowToastMessage(it.e.message.toString()))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun signup(){
        introRepository.signup(DetailSignupRequest(
            "test",
            provider = provider.value,
            nickName = nick.value,
            birthdate = birth.value,
            region = location.value,
            isMale = isMale.value
        )).onEach {
            when(it){
                is ApiState.Success -> navigateToMainActivity()
                is ApiState.Error -> {
                    _events.emit(DetailSignupEvents.ShowToastMessage(it.message))
                }
                is ApiState.Exception -> {
                    _events.emit(DetailSignupEvents.ShowToastMessage(it.e.message.toString()))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setIsMale(data: Boolean) {
        isMale.value = data
    }

    fun setBirth(birthData: String) {
        birth.value = birthData
    }

    fun setDefaultData(
        emailData: String,
        passwordData: String,
        providerData: String)
    {
        email.value = emailData
        password.value = passwordData
        provider.value = providerData
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToBack)
        }
    }

    private fun navigateToMainActivity(){
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToMainActivity)
        }
    }
}