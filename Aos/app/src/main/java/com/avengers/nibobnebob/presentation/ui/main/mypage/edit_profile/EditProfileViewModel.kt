package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class EditProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()
    
    

    val nick = MutableStateFlow("")
    val birth = MutableStateFlow("")
    val location = MutableStateFlow("")

    init {
        observeNickName()
        observeLocation()
        observeBirth()
    }

    private fun observeNickName() {
        nick.onEach {
            _uiState.update { state ->
                state.copy(
                    nickName =
                    InputState(
                        helperText = Validation.NONE,
                        isValid = false
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
                isValid = true
            )
        )
    }

    private fun observeLocation() {
        location.onEach { location ->
            _uiState.update { state ->
                state.copy(
                    location = InputState(
                        isValid = location.isNotEmpty(),
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
                        isValid = validData
                    )
                )
            }
        }.launchIn(viewModelScope)
    }


    companion object {
        val BIRTH_REGEX = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")
    }
}