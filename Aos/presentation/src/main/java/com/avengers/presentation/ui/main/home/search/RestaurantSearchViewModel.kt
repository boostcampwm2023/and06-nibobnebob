package com.avengers.nibobnebob.presentation.ui.main.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import com.avengers.nibobnebob.presentation.ui.main.home.mapper.toUiRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

data class RestaurantSearchUiState(
    val searchList: List<UiRestaurantData> = emptyList(),
    val searchKeyword: String = "",
    val isResultEmpty: Boolean = false,
)

sealed class RestaurantSearchEvent {
    data class OnClickResultItem(
        val item: UiRestaurantData
    ) : RestaurantSearchEvent()

    data object NavigateToHome : RestaurantSearchEvent()
}

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantSearchUiState())
    val uiState: StateFlow<RestaurantSearchUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantSearchEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<RestaurantSearchEvent> = _events.asSharedFlow()

    private val tempRadius = "5000000"

    private val curLongitude = MutableStateFlow("")
    private val curLatitude = MutableStateFlow("")


    fun setCurrentLocation(latitude: Double?, longitude: Double?) {
        if (latitude == null || longitude == null) return

        viewModelScope.launch {
            curLatitude.emit(latitude.toString())
            curLongitude.emit(longitude.toString())
        }
    }


    fun searchRestaurant(keyword: CharSequence) {

        if (keyword.length < 2) {
            _uiState.update { ui ->
                ui.copy(
                    searchList = emptyList(),
                    searchKeyword = "",
                    isResultEmpty = keyword.isEmpty()
                )
            }
            return
        }

        val longitude = curLongitude.value.ifEmpty { null }
        val latitude = curLatitude.value.ifEmpty { null }

        restaurantRepository.searchRestaurant(keyword.toString(), tempRadius, longitude, latitude)
            .onEach { state ->
                when (state) {
                    is BaseState.Success -> {
                        val item = state.data
                        _uiState.update { ui ->
                            ui.copy(
                                searchList = item.map { it.toUiRestaurantData() },
                                searchKeyword = keyword.toString(),
                                isResultEmpty = item.isEmpty()
                            )
                        }
                    }

                    else -> {
                        _uiState.update { ui ->
                            ui.copy(
                                searchList = emptyList(),
                                searchKeyword = "",
                                isResultEmpty = true
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun navigateToHome() {
        viewModelScope.launch {
            _events.emit(RestaurantSearchEvent.NavigateToHome)
        }
    }


    fun onClickSearchItem(item: UiRestaurantData) {
        viewModelScope.launch {
            _events.emit(RestaurantSearchEvent.OnClickResultItem(item))
        }
    }
}