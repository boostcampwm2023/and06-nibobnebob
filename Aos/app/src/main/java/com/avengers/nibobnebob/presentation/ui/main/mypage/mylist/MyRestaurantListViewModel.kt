package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.repository.RestaurantRepository
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toMyListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData
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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyRestaurantUiState(
    val myList: List<UiMyListData> = emptyList(),
    val isEmpty: Boolean = false,
)

sealed class MyRestaurantEvent {
    data class NavigateToRestaurantDetail(val id: Int) : MyRestaurantEvent()
    data class ShowSnackMessage(val msg: String) : MyRestaurantEvent()
    data class ShowToastMessage(val msg: String) : MyRestaurantEvent()
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
                is OldBaseState.Success -> {
                    _uiState.update { state ->
                        val list = my.data.body.map { it.toMyListData() }
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


    fun showDetail(id: Int) {
        viewModelScope.launch { _events.emit(MyRestaurantEvent.NavigateToRestaurantDetail(id)) }
    }

    fun deleteMyList(id: Int) {
        restaurantRepository.deleteRestaurant(id).onEach {
            when (it) {
                is OldBaseState.Success -> {
                    _events.emit(MyRestaurantEvent.ShowToastMessage("삭제 되었습니다."))
                    myRestaurantList()
                }

                is OldBaseState.Error -> {
                    _events.emit(MyRestaurantEvent.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

//    private fun updateList(deleteId : Int){
//        _uiState.update { state ->
//            state.copy(
//                myList = state.myList.filter { it.id != deleteId }.map { it }
//            )
//        }
//    }

}