package com.avengers.nibobnebob.presentation.ui.global.restaurantdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.GlobalRepository
import com.avengers.nibobnebob.presentation.ui.global.mapper.toUiRestaurantDetailInfo
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

sealed class RestaurantDetailEvents {
    data object NavigateToBack : RestaurantDetailEvents()
    data object NavigateToDetailReview : RestaurantDetailEvents()
}

data class RestaurantDetailUiState(
    val name : String = "",
    val address : String = "",
    val reviewCnt : Int = 0,
    val phoneNumber : String = "",
    val category : String = ""
)


@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val globalRepository: GlobalRepository
) : ViewModel() {
    private val TAG = "RestaurantDetailViewModelDebug"

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantDetailEvents>()
    val events: SharedFlow<RestaurantDetailEvents> = _events.asSharedFlow()

    private val restaurantId = MutableStateFlow<Int>(1)


    fun restaurantDetail(){
        globalRepository.restaurantDetail(restaurantId = restaurantId.value).onEach {
            when(it){
                is BaseState.Success -> {
                    it.data.body.toUiRestaurantDetailInfo().apply {
                        _uiState.update { state ->
                            state.copy(
                                name = name,
                                address = address,
                                reviewCnt = reviewCnt,
                                phoneNumber = phoneNumber,
                                category = category
                            )
                        }
                    }
                }
                is BaseState.Error -> {
                    Log.d(TAG,it.message)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun setRestaurantId(id : Int){
        restaurantId.value = id
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToBack)
        }
    }

    fun navigateToRestaurantDetail() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToDetailReview)
        }
    }
}