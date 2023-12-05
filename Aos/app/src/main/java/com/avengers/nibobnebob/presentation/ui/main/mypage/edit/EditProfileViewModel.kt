package com.avengers.nibobnebob.presentation.ui.main.mypage.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.repository.MyPageRepository
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.GetNickValidationUseCase
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toUiMyPageEditInfoData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import com.avengers.nibobnebob.presentation.util.LocationArray
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

data class EditProfileUiState(
    val nickName: EditInputState = EditInputState(),
    val email: String = "",
    val provider: String = "",
    val birth: EditInputState = EditInputState(),
    val location: EditInputState = EditInputState(),
    val profileImage: EditInputState = EditInputState()
)


data class EditInputState(
    val helperText: Validation = Validation.NONE,
    val isValid: Boolean = true,
    val isChanged: Boolean = false,
)

sealed class EditProfileUiEvent {
    data object EditProfileDone : EditProfileUiEvent()
    data class ShowToastMessage(val msg: String) : EditProfileUiEvent()
    data class ShowSnackMessage(val msg: String) : EditProfileUiEvent()
}


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val getNickValidationUseCase: GetNickValidationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditProfileUiEvent>(replay = 0)
    val event: SharedFlow<EditProfileUiEvent> = _events.asSharedFlow()

    private var originalNickName: String = ""
    private var originalBirth: String = ""
    private var originalLocation: String = ""
    private var originalIsMale: Boolean = true
    private var originalProfileImage: String = ""

    val locationList = LocationArray.LOCATION_ARRAY

    val nickState = MutableStateFlow("")
    val birthState = MutableStateFlow("")
    val locationState = MutableStateFlow(0)
    val locationTextState = MutableStateFlow("")
    val locationEditMode = MutableStateFlow(false)
    val profileImageState = MutableStateFlow("")


    init {
        getOriginalData()
        observeNickName()
        observeLocation()
        observeBirth()
        observeProfileImage()
    }

    private fun getOriginalData() {
        myPageRepository.getMyDefaultInfo().onEach {

            when (it) {
                is OldBaseState.Success -> {

                    it.data.body.toUiMyPageEditInfoData().apply {
                        nickState.emit(nickName)
                        locationState.emit(location.indexOf(location))
                        locationTextState.emit(location)
                        birthState.emit(birth)
                        profileImageState.emit(profileImage)

                        originalNickName = nickName
                        originalLocation = location
                        originalBirth = birth
                        originalIsMale = gender
                        originalProfileImage = profileImage

                        _uiState.update { state ->
                            state.copy(
                                email = email,
                                provider = provider
                            )
                        }
                    }
                }

                else -> _events.emit(EditProfileUiEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }

    private fun observeNickName() {
        nickState.onEach { nick ->
            if (originalNickName.isEmpty()) return@onEach
            _uiState.update { state ->

                state.copy(
                    nickName = EditInputState(
                        helperText = Validation.NONE,
                        isValid = (originalNickName == nick && state.nickName.helperText == Validation.NONE),
                        isChanged = originalNickName != nick
                    )
                )
            }
        }.launchIn(viewModelScope)

    }

    fun checkNickValidation() {
        getNickValidationUseCase(nickState.value).onEach {
            when (it) {
                is BaseState.Success -> {
                    if (it.data.isExist) {

                        _uiState.value = uiState.value.copy(
                            nickName = EditInputState(
                                helperText = Validation.INVALID_NICK,
                                isValid = false,
                                isChanged = false
                            )
                        )
                    } else {
                        _uiState.value = uiState.value.copy(
                            nickName = EditInputState(
                                helperText = Validation.VALID_NICK,
                                isValid = true,
                                isChanged = (originalNickName != nickState.value)
                            )
                        )
                    }
                }

                else -> _events.emit(EditProfileUiEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)

    }

    private fun observeLocation() {
        locationState.onEach { position ->
            _uiState.update { state ->
                state.copy(
                    location = EditInputState(
                        isValid = true,
                        isChanged = if (position == 0 || originalLocation == locationList[locationState.value]) false
                        else locationEditMode.value
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setLocationEditMode() {
        viewModelScope.launch { locationEditMode.emit(true) }
    }

    fun setBirth(birthData: String) {
        viewModelScope.launch {
            birthState.emit(birthData)
        }
    }


    private fun observeBirth() {
        birthState.onEach { birth ->
            if (originalBirth.isEmpty()) return@onEach
            val validData = birth.matches(BIRTH_REGEX)
            _uiState.update { state ->
                state.copy(
                    birth = EditInputState(
                        helperText = if (!validData && birth.isNotEmpty()) Validation.INVALID_DATE else Validation.VALID_DATE,
                        isValid = validData,
                        isChanged = originalBirth != birth
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeProfileImage() {
        profileImageState.onEach { image ->
            if (originalProfileImage.isEmpty()) return@onEach
            _uiState.update { state ->
                state.copy(
                    profileImage = EditInputState(
                        isChanged = (originalProfileImage != profileImageState.value)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateProfileImage(imageUri: String) {
        profileImageState.update { imageUri }
    }


    fun doneEditProfile() {

        myPageRepository.editMyInfo(
            EditMyInfoRequest(
                nickName = nickState.value,
                email = uiState.value.email,
                provider = uiState.value.provider,
                birthdate = birthState.value,
                region = if (locationState.value == 0) originalLocation else locationList[locationState.value],
                isMale = originalIsMale,
                password = "1234",
                profileImage = profileImageState.value
            )
        ).onEach {
            when (it) {
                is OldBaseState.Success -> _events.emit(EditProfileUiEvent.EditProfileDone)
                else -> _events.emit(EditProfileUiEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }


    companion object {
        val BIRTH_REGEX = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")
    }
}