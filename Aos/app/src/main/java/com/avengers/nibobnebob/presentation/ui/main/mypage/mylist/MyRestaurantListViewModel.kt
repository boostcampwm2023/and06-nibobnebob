package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.RestaurantRepository
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toMyPageListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageListData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
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
import javax.inject.Inject

data class MyRestaurantUiState(
    val myList: List<UiMyPageListData> = emptyList(),
    val isEmpty: Boolean = false,
)

sealed class MyRestaurantEvent {
    data class ShowSnackMessage(val msg: String) : MyRestaurantEvent()
}


@HiltViewModel
class MyRestaurantListViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyRestaurantUiState())
    val uiState: StateFlow<MyRestaurantUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyRestaurantEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<MyRestaurantEvent> = _events.asSharedFlow()


    fun myRestaurantList() {
        restaurantRepository.myRestaurantList().onEach { my ->
            when (my) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        val list = my.data.body.map { it.toMyPageListData() }
                        state.copy(
                            myList = list,
                            isEmpty = list.isEmpty()
                        )
                    }
                }

                else -> _events.emit(MyRestaurantEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }


    fun clickItem(id: Int) {
        Log.d("TEST", "$id")
    }

}