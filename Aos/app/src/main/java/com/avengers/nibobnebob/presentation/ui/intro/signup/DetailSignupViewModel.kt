package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class DetailSignupUiState(
    val nickState: InputState = InputState.Empty,
    val birthState: InputState = InputState.Empty,
)

sealed class InputState{
    data object Empty: InputState()
    data class Success(val msg: String): InputState()
    data class Error(val msg: String): InputState()
}

sealed class DetailSignupEvents{
    data object NavigateToBack : DetailSignupEvents()
    data object NavigateToMainActivity: DetailSignupEvents()
}

@HiltViewModel
class DetailSignupViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(DetailSignupUiState())
    val uiState: StateFlow<DetailSignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DetailSignupEvents>()
    val events: SharedFlow<DetailSignupEvents> = _events.asSharedFlow()

}