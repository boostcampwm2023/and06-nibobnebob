package com.avengers.nibobnebob.presentation.ui.main.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val locationTrackingState: TrackingState = TrackingState.TryOn
)

sealed class TrackingState {
    data object TryOn : TrackingState()
    data object On : TrackingState()
    data object Off : TrackingState()
}

sealed class HomeEvents{
    data object NavigateToSearchRestaurant: HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun locationBtnClicked() {
        _uiState.update { state ->
            state.copy(
                locationTrackingState = if (_uiState.value.locationTrackingState == TrackingState.Off) TrackingState.TryOn
                else TrackingState.Off
            )
        }
    }

    fun trackingOn() {
        _uiState.update { state ->
            state.copy(
                locationTrackingState = TrackingState.On
            )
        }
    }

    fun trackingOff() {
        _uiState.update { state ->
            state.copy(
                locationTrackingState = TrackingState.Off
            )
        }
    }

    fun navigateToSearchRestaurant(){
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToSearchRestaurant)
        }
    }

}