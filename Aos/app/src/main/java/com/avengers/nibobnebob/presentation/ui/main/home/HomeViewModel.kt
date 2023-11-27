package com.avengers.nibobnebob.presentation.ui.main.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.HomeRepository
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiFilterData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiMarkerData
import com.avengers.nibobnebob.presentation.util.Constants.MY_LIST
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

data class HomeUiState(
    val locationTrackingState: TrackingState = TrackingState.TryOn,
    val filterList: List<UiFilterData> = emptyList(),
    val markerList: List<UiMarkerData> = emptyList(),
    val curFilter: String = MY_LIST
)

sealed class TrackingState {
    data object TryOn : TrackingState()
    data object On : TrackingState()
    data object Off : TrackingState()
}

sealed class HomeEvents {
    data object NavigateToSearchRestaurant : HomeEvents()
    data object SetNewMarkers: HomeEvents()
    data object RemoveMarkers: HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

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

    fun getFilterList() {
        homeRepository.followList().onEach {
            when(it){
                is BaseState.Success -> {
                    val initialFilterData = UiFilterData(MY_LIST, true, ::onFilterItemClicked)
                    val filterList = listOf(initialFilterData) +  it.data.body.map { name ->
                        UiFilterData(name, false, ::onFilterItemClicked)
                    }
                    _uiState.update { state ->
                        state.copy(
                            filterList = filterList,
                            curFilter = MY_LIST
                        )
                    }
                }
                is BaseState.Error ->{
                    _uiState.update { state ->
                        state.copy(
                            filterList = listOf(UiFilterData(MY_LIST, true, ::onFilterItemClicked)),
                            curFilter = MY_LIST
                        )
                    }
                    Log.d("FilterListDebug","팔로우 리스트를 받아오지 못하였습니다. default 마이리스트")
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getMarkerList() {
        // todo 현재 필터를 서버에 보내서, 데이터 가져오기

        // todo 데이터 성공적 수신시, setMarker 하기
        viewModelScope.launch {

            _events.emit(HomeEvents.RemoveMarkers)

            when (_uiState.value.curFilter) {
                MY_LIST -> {
                    _uiState.update { state ->
                        state.copy(
                            markerList = listOf(
                                UiMarkerData(
                                    id = 1,
                                    latitude = 37.355594049034,
                                    longitude = 126.36707115682,
                                    name = "너무 맛있는 집",
                                    address = "서울시 중구 만리동",
                                    phoneNumber = "010-1234-5254",
                                    reviewCount = "99개",
                                    isInWishList = false,
                                    isInMyList = false
                                ),
                                UiMarkerData(
                                    id = 1,
                                    latitude = 37.555594049034,
                                    longitude = 126.96707115682,
                                    name = "그닥 맛없는 집",
                                    address = "서울시 중구 용현동",
                                    phoneNumber = "010-1234-5254",
                                    reviewCount = "90개",
                                    isInWishList = false,
                                    isInMyList = false
                                ),
                                UiMarkerData(
                                    id = 1,
                                    latitude = 37.255594049034,
                                    longitude = 126.16707115682,
                                    name = "그럭저럭?",
                                    address = "서울시 중구 만리동",
                                    phoneNumber = "010-1234-5254",
                                    reviewCount = "99개",
                                    isInWishList = false,
                                    isInMyList = false
                                ),
                            )
                        )
                    }
                }

                "K011 노균욱" -> {
                    _uiState.update { state ->
                        state.copy(
                            markerList = listOf(
                                UiMarkerData(
                                    id = 1,
                                    latitude = 36.555594049034,
                                    longitude = 125.96707115682,
                                    name = "세영국밥",
                                    address = "서울시 영등포구",
                                    phoneNumber = "010-1234-5111",
                                    reviewCount = "0개",
                                    isInWishList = false,
                                    isInMyList = false
                                ),
                                UiMarkerData(
                                    id = 1,
                                    latitude = 37.355594049034,
                                    longitude = 125.96707115682,
                                    name = "균욱불뼈찜",
                                    address = "서울시 중구 만리동",
                                    phoneNumber = "010-1234-2254",
                                    reviewCount = "90개",
                                    isInWishList = true,
                                    isInMyList = false
                                ),
                                UiMarkerData(
                                    id = 1,
                                    latitude = 37.255594049034,
                                    longitude = 126.76707115682,
                                    name = "진성아구찜",
                                    address = "서울시 중구 만리동",
                                    phoneNumber = "010-1234-5254",
                                    reviewCount = "99개",
                                    isInWishList = false,
                                    isInMyList = true
                                ),
                            )
                        )
                    }
                }
            }

            _events.emit(HomeEvents.SetNewMarkers)
        }
    }

    private fun onFilterItemClicked(name: String) {
        _uiState.update { state ->
            state.copy(
                curFilter = name,
                filterList = state.filterList.map {
                    if (it.name == name) {
                        it.copy(
                            isSelected = true
                        )
                    } else if (it.isSelected) {
                        it.copy(
                            isSelected = false
                        )
                    } else {
                        it
                    }
                },
                locationTrackingState = TrackingState.Off
            )
        }

        getMarkerList()
    }

    fun navigateToSearchRestaurant() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToSearchRestaurant)
        }
    }

}