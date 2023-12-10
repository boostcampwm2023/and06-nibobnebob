package com.avengers.nibobnebob.presentation.ui.main.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.FollowRepository
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import com.avengers.nibobnebob.domain.usecase.restaurant.AddWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteMyWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.GetMyRestaurantListUseCase
import com.avengers.nibobnebob.presentation.ui.main.home.mapper.toUiRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiFilterData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRecommendRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import com.avengers.nibobnebob.presentation.util.Constants.MY_LIST
import com.avengers.nibobnebob.presentation.util.Constants.NEAR_RESTAURANT
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
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
import java.lang.Math.pow
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class HomeUiState(
    val locationTrackingState: TrackingState = TrackingState.TryOn,
    val filterList: List<UiFilterData> = emptyList(),
    val markerList: List<UiRestaurantData> = emptyList(),
    val recommendList: List<UiRecommendRestaurantData> = emptyList(),
    val curFilter: String = MY_LIST,
    val cameraLatitude: Double = 37.553836,
    val cameraLongitude: Double = 126.969652,
    val cameraZoom: Double = 12.0,
    val cameraRadius: Double = 0.0,
    val curLatitude: Double = 0.0,
    val curLongitude: Double = 0.0,
    val curSelectedMarker: Marker? = null,
    val addRestaurantId: Int = -1
)

sealed class TrackingState {
    data object TryOn : TrackingState()
    data object On : TrackingState()
    data object Off : TrackingState()
}

sealed class HomeEvents {
    data object NavigateToSearchRestaurant : HomeEvents()
    data object SetNewMarkers : HomeEvents()
    data class SetSingleMarker(
        val marker: Marker?,
        val item: UiRestaurantData
    ) : HomeEvents()

    data object ShowRecommendRestaurantDialog : HomeEvents()

    data object RemoveMarkers : HomeEvents()
    data class ShowSnackMessage(
        val msg: String
    ) : HomeEvents()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val followRepository: FollowRepository,
    private val restaurantRepository: RestaurantRepository,
    private val myRestaurantListUseCase: GetMyRestaurantListUseCase,
    private val addWishRestaurantUseCase: AddWishRestaurantUseCase,
    private val deleteMyWishRestaurantUseCase: DeleteMyWishRestaurantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<HomeEvents> = _events.asSharedFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        _uiState.update { state ->
            state.copy(
                curLatitude = latitude,
                curLongitude = longitude
            )
        }
    }

    fun updateCamera(latitude: Double, longitude: Double, zoom: Double) {
        _uiState.update { state ->
            state.copy(
                cameraRadius = pow(2.0, 14.0 - uiState.value.cameraZoom) * 1000,
                cameraLatitude = latitude,
                cameraLongitude = longitude,
                cameraZoom = zoom
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

    fun recommendRestaurantList() {
        restaurantRepository.recommendRestaurantList().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            recommendList = it.data.map { recommend ->
                                UiRecommendRestaurantData(
                                    category = recommend.category,
                                    id = recommend.id,
                                    name = recommend.name,
                                    reviewImage = recommend.reviewImage,
                                )
                            }
                        )
                    }
                    _events.emit(HomeEvents.ShowRecommendRestaurantDialog)
                }

                is BaseState.Error -> {
                    _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFilterList() {
        followRepository.getMyFollowing().onEach { it ->
            val initialFilterList = listOf(
                UiFilterData(MY_LIST, isChecked(MY_LIST), ::onFilterItemClicked),
                UiFilterData(NEAR_RESTAURANT, isChecked(NEAR_RESTAURANT), ::onFilterItemClicked)
            )
            when (it) {
                is BaseState.Success -> {
                    val filterList = initialFilterList + it.data.map {
                        UiFilterData(it.nickName, isChecked(it.nickName), ::onFilterItemClicked)
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
                            filterList = initialFilterList,
                            curFilter = MY_LIST
                        )
                    }
                    _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun isChecked(filterName: String): Boolean {
        return filterName == uiState.value.curFilter
    }

    fun updateNearRestaurant() {
        onFilterItemClicked(NEAR_RESTAURANT)
    }

    fun getMarkerList() {
        when (uiState.value.curFilter) {
            NEAR_RESTAURANT -> nearRestaurantList()
            MY_LIST -> myRestaurantList()
            else -> userRestaurantList()
        }
    }

    private fun nearRestaurantList() {
        restaurantRepository.nearRestaurantList(
            radius = uiState.value.cameraRadius.toString(),
            longitude = uiState.value.cameraLongitude.toString(),
            latitude = uiState.value.cameraLatitude.toString()
        ).onEach {
            _events.emit(HomeEvents.RemoveMarkers)
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            curFilter = NEAR_RESTAURANT,
                            markerList = it.data.map { restaurants ->
                                restaurants.toUiRestaurantData()
                            }
                        )
                    }
                    moveCamera()
                }

                is BaseState.Error -> {
                    _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
                }
            }
            _events.emit(HomeEvents.SetNewMarkers)
        }.launchIn(viewModelScope)

    }

    private fun myRestaurantList() {
        myRestaurantListUseCase().onEach {
            _events.emit(HomeEvents.RemoveMarkers)
            when (it) {
                is BaseState.Success -> {
                    it.data.restaurantItemsData?.let { restaurants ->
                        val restaurantsList = restaurants.map { data ->
                            data.toUiRestaurantData()
                        }
                        _uiState.update { state ->
                            state.copy(
                                curFilter = MY_LIST,
                                markerList = restaurantsList
                            )
                        }
                    }
                    moveCamera()
                }

                is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
            }
            _events.emit(HomeEvents.SetNewMarkers)
        }.launchIn(viewModelScope)
    }

    private fun userRestaurantList() {
        restaurantRepository.filterRestaurantList(
            _uiState.value.curFilter,
            "${_uiState.value.curLatitude} ${_uiState.value.curLongitude}",
            50000
        ).onEach {
            _events.emit(HomeEvents.RemoveMarkers)
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            markerList = it.data.map { restaurants ->
                                restaurants.toUiRestaurantData()
                            }
                        )
                    }
                    moveCamera()
                }

                is BaseState.Error -> _events.emit(HomeEvents.ShowSnackMessage(ERROR_MSG))
            }
            _events.emit(HomeEvents.SetNewMarkers)
        }.launchIn(viewModelScope)
    }

    suspend fun updateWish(id: Int, curState: Boolean): Boolean {

        val result: Boolean = viewModelScope.async {
            var flag = true
            if (curState) {
                deleteMyWishRestaurantUseCase(id).onEach {
                    flag = when (it) {
                        is BaseState.Success -> true
                        else -> false
                    }
                }.launchIn(viewModelScope)
                flag
            } else {
                addWishRestaurantUseCase(id).onEach {
                    flag = when (it) {
                        is BaseState.Success -> true
                        else -> false
                    }
                }.launchIn(viewModelScope)
                flag
            }

        }.await()



        if (result) {
            _uiState.update { state ->
                state.copy(
                    markerList = uiState.value.markerList.map {
                        if (it.id == id) {
                            it.copy(isInWishList = !curState)
                        } else it
                    }
                )
            }
            _events.emit(
                HomeEvents.SetSingleMarker(
                    uiState.value.curSelectedMarker,
                    uiState.value.markerList.find { it.id == id }!!
                )
            )
        }

        return result


    }

    fun setSelectedMarker(marker: Marker) {
        _uiState.update { state ->
            state.copy(curSelectedMarker = marker)
        }
    }

    private fun calculateDensity(latitude: Double, longitude: Double): Int {
        var density = 0

        for (point in uiState.value.markerList) {
            val distance = haversineDistance(latitude, longitude, point.latitude, point.longitude)
            if (distance <= uiState.value.cameraRadius) {
                density++
            } else {
                break
            }
        }

        return density
    }

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radius = 6371000 // 지구 반지름(미터 단위)
        val distanceLatitude = Math.toRadians(lat2 - lat1)
        val distanceLongitude = Math.toRadians(lon2 - lon1)

        val a = (sin(distanceLatitude / 2) * sin(distanceLatitude / 2)) +
                (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                        sin(distanceLongitude / 2) * sin(distanceLongitude / 2))

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun moveCamera() {
        if (_uiState.value.markerList.isEmpty()) return
        if (uiState.value.addRestaurantId != 0) {
            val restaurantItem: UiRestaurantData =
                uiState.value.markerList.first { it.id == uiState.value.addRestaurantId }
            _uiState.update { state ->
                state.copy(
                    addRestaurantId = 0,
                    cameraLongitude = restaurantItem.longitude,
                    cameraLatitude = restaurantItem.latitude
                )
            }
            return
        }

        var closestPoint: LatLng? = null
        var maxDensityPoint: LatLng? = null
        var minDistance = Double.MAX_VALUE
        var maxDensity = 0

        for (point in _uiState.value.markerList) {
            val distance = haversineDistance(
                _uiState.value.cameraLatitude,
                _uiState.value.cameraLongitude,
                point.latitude,
                point.longitude
            )
            val density = calculateDensity(
                _uiState.value.cameraLatitude,
                _uiState.value.cameraLatitude,
            )

            if (distance < minDistance) {
                minDistance = distance
                closestPoint = LatLng(point.latitude, point.longitude)
            }

            if (density > maxDensity) {
                maxDensity = density
                maxDensityPoint = LatLng(point.latitude, point.longitude)
            }
        }

        val targetPoint = maxDensityPoint ?: closestPoint
        targetPoint?.let {
            _uiState.update { state ->
                state.copy(
                    cameraLatitude = targetPoint.latitude,
                    cameraLongitude = targetPoint.longitude
                )
            }
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
                    } else {
                        it.copy(
                            isSelected = false
                        )
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

    fun setAddRestaurantId(restaurantId: Int) {
        if (restaurantId != -1) {
            _uiState.update { state ->
                state.copy(
                    addRestaurantId = restaurantId
                )
            }
        }
    }

}