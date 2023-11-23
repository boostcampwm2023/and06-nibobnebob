package com.avengers.nibobnebob.presentation.ui.main.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiFilterData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantSimpleData
import com.avengers.nibobnebob.presentation.util.Constants.MY_LIST
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
    val locationTrackingState: TrackingState = TrackingState.TryOn,
    val filterList: List<UiFilterData> = emptyList(),
    val markerList: List<UiRestaurantSimpleData> = emptyList(),
    val curFilter: String = MY_LIST
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

    fun getFilterList(){
        _uiState.update { state ->
            state.copy(
                filterList = listOf(
                    UiFilterData(MY_LIST, true),
                    UiFilterData("K011 노균욱", false),
                    UiFilterData("K015 박진성", false),
                    UiFilterData("K024 오세영", false),
                )
            )
        }
    }

    fun getMarkerList(){
        // todo 현재 필터를 서버에 보내서, 데이터 가져오기
        
        // todo 데이터 성공적 수신시, setMarker 하기
    }

    private fun onFilterItemClicked(name: String){
        _uiState.update { state ->
            state.copy(
                curFilter = name,
                filterList = state.filterList.map {
                    it.isSelected = it.name == name
                    it
                }
            )
        }
    }

    fun navigateToSearchRestaurant(){
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToSearchRestaurant)
        }
    }

}