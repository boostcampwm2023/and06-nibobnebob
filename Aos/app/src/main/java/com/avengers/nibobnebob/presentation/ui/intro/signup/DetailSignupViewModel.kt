package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.util.Validation
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

data class DetailSignupUiState(
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
}

@HiltViewModel
class DetailSignupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DetailSignupUiState())
    val uiState: StateFlow<DetailSignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DetailSignupEvents>()
    val events: SharedFlow<DetailSignupEvents> = _events.asSharedFlow()

    val nick = MutableStateFlow("")
    private var nickValidation = false
    private val gender = MutableStateFlow("male")
    val birth = MutableStateFlow("")
    val location = MutableStateFlow("")

    val isDataReady = combine(nick, gender, birth, location) { nick, gender, birth, location ->
        nick.isNotBlank() && gender.isNotBlank() && birth.isNotBlank() && location.isNotBlank() &&
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
                    nickState = InputState.Empty
                )
            }
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
                        birthState = InputState.Error("올바른 이메일 형식이 아닙니다")
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkNickDuplication() {
        viewModelScope.launch {

            // todo 중복체크 성공일때
            nickValidation = true
            _uiState.update { state ->
                state.copy(
                    nickState = InputState.Success("사용 가능한 닉네임 입니다")
                )
            }

            // todo 중복체크 실패일때
//            nickValidation = false
//            _uiState.update { state ->
//                state.copy(
//                    nickState = InputState.Error("이미 사용중인 닉네임 입니다")
//                )
//            }
        }
    }

    fun setGender(genderData: Gender) {
        gender.value = genderData.data
    }

    fun setBirth(birthData: String) {
        birth.value = birthData
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToBack)
        }
    }

    fun navigateToMainActivity(){
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToMainActivity)
        }
    }
}

enum class Gender(val data: String) {
    MALE("male"),
    FEMALE("female")
}