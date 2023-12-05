package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.IntroRepository
import com.avengers.nibobnebob.data.repository.ValidationRepository
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class DetailSignupUiState(
    val isNickNotEmpty: Boolean = false,
    val nickState: InputState = InputState.Empty,
    val birthState: InputState = InputState.Empty,
)

sealed class DetailSignupEvents {
    data object NavigateToBack : DetailSignupEvents()
    data object NavigateToLoginFragment : DetailSignupEvents()
    data class ShowSnackMessage(val msg: String) : DetailSignupEvents()
    data object OpenGallery : DetailSignupEvents()
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
    val profileImg = MutableStateFlow("")
    private lateinit var profileFile: MultipartBody.Part

    val isDataReady =
        combine(nick, birth, location, nickValidation, profileImg) { nick, birth, location, nickValidation, profileImg->
            nick.isNotBlank() && birth.isNotBlank() && location.isNotBlank() &&
                    nickValidation && profileImg.isNotBlank()
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
            if (ValidationUtil.checkBirth(it) || it.isBlank()) {
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

            when (it) {
                is BaseState.Success -> {
                    if (it.data.body.isExist) {
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

                is BaseState.Error -> _events.emit(DetailSignupEvents.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }

    fun signup() {
        // todo MultiPart -> url API를 따로 만들기 or signup Request 자체를 MultiPart로 묶기
        introRepository.signup(
            email = email.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            provider = provider.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            password = password.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            nickName = nick.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            birthdate = birth.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            region = location.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            isMale = isMale.value,
            profileImage = profileFile
        ).onEach {
            when (it) {
                is BaseState.Success -> navigateToLoginFragment()
                is BaseState.Error -> _events.emit(DetailSignupEvents.ShowSnackMessage(ERROR_MSG))
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
        providerData: String
    ) {
        email.value = emailData
        password.value = passwordData
        provider.value = providerData
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToBack)
        }
    }

    private fun navigateToLoginFragment() {
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.NavigateToLoginFragment)
        }
    }

    fun openGallery(){
        viewModelScope.launch {
            _events.emit(DetailSignupEvents.OpenGallery)
        }
    }

    fun setImage(uri: String, file: MultipartBody.Part){
        profileImg.value = uri
        profileFile = file
    }
}