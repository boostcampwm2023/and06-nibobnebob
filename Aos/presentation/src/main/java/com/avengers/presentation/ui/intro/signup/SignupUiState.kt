package com.avengers.nibobnebob.presentation.ui.intro.signup

sealed class InputState {
    data object Empty : InputState()
    data class Success(val msg: String) : InputState()
    data class Error(val msg: String) : InputState()
}