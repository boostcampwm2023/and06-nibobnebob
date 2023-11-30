package com.avengers.nibobnebob.presentation.ui.main.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.HomeRepository
import com.avengers.nibobnebob.data.repository.RestaurantRepository
import com.avengers.nibobnebob.presentation.ui.main.home.mapper.toUiRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiFilterData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import com.avengers.nibobnebob.presentation.util.Constants.MY_LIST
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class HomeUiState(
    val locationTrackingState: TrackingState = TrackingState.TryOn,
    val filterList: List<UiFilterData> = emptyList(),
    val markerList: List<UiRestaurantData> = emptyList(),
    val curFilter: String = MY_LIST,
    val cameraLatitude: Double = 0.0,
    val cameraLongitude: Double = 0.0,
    val cameraBound: LatLngBounds = LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)),
    val curLatitude: Double = 0.0,
    val curLongitude: Double = 0.0
)

sealed class TrackingState {
    data object TryOn : TrackingState()
    data object On : TrackingState()
    data object Off : TrackingState()
}

sealed class HomeEvents {
    data object NavigateToSearchRestaurant : HomeEvents()
    data object SetNewMarkers : HomeEvents()
    data object RemoveMarkers : HomeEvents()
    data class ShowSnackMessage(
        val msg: String
    ) : HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>()
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            state.copy(
                curLatitude = latitude,
                curLongitude = longitude
            )
        }
    }

    fun updateCamera(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            state.copy(
                cameraLatitude = latitude,
                cameraLongitude = longitude
            )
        }
    }

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
        homeRepository.followList().onEach { it ->
            when (it) {
                is BaseState.Success -> {
                    val initialFilterData = UiFilterData(MY_LIST, true, ::onFilterItemClicked)
                    val filterList = listOf(initialFilterData) + it.data.body.map {
                        UiFilterData(it.nickName, false, ::onFilterItemClicked)
                    }
                    _uiState.update { state ->
                        state.copy(
                            filterList = filterList,
                            curFilter = MY_LIST
                        )
                    }
                }

                is BaseState.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            filterList = listOf(UiFilterData(MY_LIST, true, ::onFilterItemClicked)),
                            curFilter = MY_LIST
                        )
                    }
                    _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                }
            }

        }.launchIn(viewModelScope)
    }

    fun getMarkerList() {
        when (_uiState.value.curFilter) {
            MY_LIST -> {
                restaurantRepository.myRestaurantList().onEach {
                    _events.emit(HomeEvents.RemoveMarkers)
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(markerList = it.data.body.map { data ->
                                    data.toUiRestaurantData()
                                })
                            }
                            moveCamera()
                        }

                        is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                    }
                    _events.emit(HomeEvents.SetNewMarkers)
                }.launchIn(viewModelScope)
            }

            else -> {
                homeRepository.filterRestaurantList(
                    _uiState.value.curFilter,
                    "${_uiState.value.curLatitude} ${_uiState.value.curLongitude}",
                    50000
                ).onEach {
                    _events.emit(HomeEvents.RemoveMarkers)
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(markerList = it.data.body.map { data ->
                                    data.toUiRestaurantData()
                                })
                            }
                            moveCamera()
                        }

                        is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                    }
                    _events.emit(HomeEvents.SetNewMarkers)
                }.launchIn(viewModelScope)
            }
        }
    }

//TODO 문제 -> 다 보여주려면 ZOOM을 축소 고정 -> 다 가까울 때 문제

//        private fun moveCamera() {
//        val list = _uiState.value.markerList
//        val markerSize = list.size
//        val averageLatitude = list.sumOf { it.latitude }
//        val averageLongitude = list.sumOf { it.longitude }
//
//        _uiState.update { state ->
//            state.copy(
//                cameraLatitude = averageLatitude / markerSize,
//                cameraLongitude = averageLongitude / markerSize
//            )
//        }
//    }


// //TODO 해결 1. 현재 카메라의 위치를 기준으로 가장 가가운 곳으로 줌 레벨을 설정
//
//    private fun haversineDistance(latitude: Double, longitude: Double): Double {
//        val radius = 6371
//        val distanceLatitude = Math.toRadians(_uiState.value.cameraLatitude - latitude)
//        val distanceLongitude = Math.toRadians(_uiState.value.cameraLongitude - longitude)
//
//        val a = (sin(distanceLatitude / 2) * sin(distanceLatitude / 2)) +
//                (cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude)) *
//                        sin(distanceLongitude / 2) * sin(distanceLongitude / 2))
//
//        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
//        return radius * c
//    }
//
//    private fun moveCamera() {
//        if (_uiState.value.markerList.isEmpty()) return
//
//        var closestPoint: LatLng? = null
//        var minDistance = Double.MAX_VALUE
//
//        for (point in _uiState.value.markerList) {
//            val distance = haversineDistance(point.latitude,point.longitude)
//            if (distance < minDistance) {
//                minDistance = distance
//                closestPoint = LatLng(point.latitude,point.longitude)
//            }
//        }
//        closestPoint?.let {
//            _uiState.update { state ->
//                state.copy(
//                    cameraLatitude = closestPoint.latitude,
//                    cameraLongitude = closestPoint.longitude
//                )
//            }
//        }
//    }

    private fun findExtremeCoordinates(): Pair<LatLng, LatLng> {

        var minLatitude = Double.MAX_VALUE
        var maxLatitude = Double.MIN_VALUE
        var minLongitude = Double.MAX_VALUE
        var maxLongitude = Double.MIN_VALUE

        for (point in _uiState.value.markerList) {
            if (point.latitude < minLatitude) {
                minLatitude = point.latitude
            }
            if (point.latitude > maxLatitude) {
                maxLatitude = point.latitude
            }
            if (point.longitude < minLongitude) {
                minLongitude = point.longitude
            }
            if (point.longitude > maxLongitude) {
                maxLongitude = point.longitude
            }
        }


        val padding = 0.03
        val minCoordinates = LatLng(minLatitude, minLongitude-padding)
        val maxCoordinates = LatLng(maxLatitude, maxLongitude+padding)

        return Pair(minCoordinates, maxCoordinates)
    }

    private fun moveCamera() {
        val coordinates = findExtremeCoordinates()
        _uiState.update {state ->
            state.copy(
                cameraBound = LatLngBounds(coordinates.first,coordinates.second)
            )
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

    fun addWishTest(id: Int) {
        restaurantRepository.addWishList(id).onEach {
            when (it) {
                is BaseState.Success -> Log.d("TEST", "success")
                is BaseState.Error -> Log.d("TEST", "fail")
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToSearchRestaurant() {
        viewModelScope.launch {
            _events.emit(HomeEvents.NavigateToSearchRestaurant)
        }
    }

}