package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation

data class EditProfileUiState(
    val nickName: InputState = InputState(),
    val birth : InputState = InputState(),
    val location : InputState = InputState()
)


data class InputState(
    val helperText : Validation = Validation.NONE,
    val isValid : Boolean = false,
)
