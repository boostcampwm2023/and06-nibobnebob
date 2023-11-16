package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

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


class EditProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    var originalNickName: String = ""
    var originalBirth: String = ""
    var originalLocation: String = ""

    val nick = MutableStateFlow("")
    val birth = MutableStateFlow("")
    val location = MutableStateFlow("")

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
        }.onEach {
            originalNickName = it.originalNickName
            nick.emit(it.originalNickName)
            originalBirth = it.originalBirth
            birth.emit(it.originalBirth)
            originalLocation = it.originalLocation
            location.emit(it.originalLocation)
        }.launchIn(viewModelScope)
    }

    private fun observeNickName() {
        nick.onEach { nick ->
            _uiState.update { state ->
                state.copy(
                    nickName =
                    InputState(
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
        location.onEach { location ->
            _uiState.update { state ->
                state.copy(
                    location = InputState(
                        isValid = location.isNotEmpty(),
                        isChanged = originalLocation != location
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
                        isChanged = originalBirth != birth
                    )
                )
            }
        }.launchIn(viewModelScope)
    }


    companion object {
        val BIRTH_REGEX = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")
    }
}