package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.avengers.nibobnebob.presentation.util.LocationArray
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val nickName: InputState = InputState(),
    val birth: InputState = InputState(),
    val location: InputState = InputState()
)


data class InputState(
    val helperText: Validation = Validation.NONE,
    val isValid: Boolean = true,
    val isChanged: Boolean = false,
)

data class OriginalState(
    val originalNickName: String,
    val originalBirth: String,
    val originalLocation: String
)

sealed class EditProfileUiEvent {
    data object EditProfileDone : EditProfileUiEvent()
}


class EditProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditProfileUiEvent>(replay = 0)
    val event: SharedFlow<EditProfileUiEvent> = _events.asSharedFlow()

    private var originalNickName: String = ""
    private var originalBirth: String = ""
    private var originalLocation: String = ""

    val locationList = LocationArray.LOCATION_ARRAY

    val nick = MutableStateFlow("")
    val birth = MutableStateFlow("")
    val locationPosition = MutableStateFlow(0)


    init {
        observeNickName()
        observeLocation()
        observeBirth()
        getOriginalData()
    }

    private fun getOriginalData() {
        flow {
            emit(
                OriginalState(
                    originalNickName = "tester",
                    originalBirth = "2000/03/13",
                    originalLocation = "용산구"
                )
            )
        }.onEach { state ->

            originalNickName = state.originalNickName
            originalBirth = state.originalBirth
            originalLocation = state.originalLocation

            nick.emit(state.originalNickName)
            birth.emit(state.originalBirth)
            locationPosition.emit(locationList.indexOf(state.originalLocation))

        }.launchIn(viewModelScope)
    }

    private fun observeNickName() {
        nick.onEach { nick ->
            _uiState.update { state ->
                state.copy(
                    nickName = InputState(
                        helperText = Validation.NONE,
                        isValid = originalNickName == nick,
                        isChanged = originalNickName != nick
                    )
                )
            }
        }.launchIn(viewModelScope)

    }

    fun checkNickValidation() {
        // check(nickName) 서버에서 검증
        _uiState.value = uiState.value.copy(
            nickName = InputState(
                helperText = Validation.VALID_NICK,
                isValid = true,
                isChanged = originalNickName != nick.value
            )
        )
    }

    private fun observeLocation() {
        locationPosition.onEach { position ->
            _uiState.update { state ->
                state.copy(
                    location = InputState(
                        isValid = (position != 0),
                        isChanged = locationList.indexOf(originalLocation) != position
                    )
                )
            }
        }.launchIn(viewModelScope)


    }

    fun setBirth(birthData: String) {
        birth.value = birthData
    }


    private fun observeBirth() {
        birth.onEach { birth ->
            val validData = birth.matches(BIRTH_REGEX)
            _uiState.update { state ->
                state.copy(
                    birth = InputState(
                        helperText = if (!validData && birth.isNotEmpty()) Validation.INVALID_DATE else Validation.VALID_DATE,
                        isValid = validData,
                        isChanged = (originalBirth != birth)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }


    fun doneEditProfile() {
        // 서버로 전송, 응답 200 이면 실행
        viewModelScope.launch {
            _events.emit(EditProfileUiEvent.EditProfileDone)
        }
    }


    companion object {
        val BIRTH_REGEX = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")
    }
}