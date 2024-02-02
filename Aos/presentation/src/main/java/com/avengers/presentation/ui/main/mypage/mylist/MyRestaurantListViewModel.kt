package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.GetMyRestaurantListUseCase
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toUiMyListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.wishlist.MyWishListViewModel
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_NEW
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_OLD
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
    val filterOption: String = "",
    val listPage: Int = 1,
    val lastPage: Boolean = false,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class MyRestaurantEvent {
    data class NavigateToRestaurantDetail(val id: Int) : MyRestaurantEvent()
    data class ShowSnackMessage(val msg: String) : MyRestaurantEvent()
    data class ShowToastMessage(val msg: String) : MyRestaurantEvent()
}


@HiltViewModel
class MyRestaurantListViewModel @Inject constructor(
    private val getMyRestaurantListUseCase: GetMyRestaurantListUseCase,
    private val deleteRestaurantUseCase: DeleteRestaurantUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyRestaurantUiState())
    val uiState: StateFlow<MyRestaurantUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyRestaurantEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<MyRestaurantEvent> = _events.asSharedFlow()


    fun myRestaurantList(
        sort: String? = null,
    ) {
        getMyRestaurantListUseCase(
            limit = ITEM_LIMIT, page = FIRST_PAGE, sort = sort
                ?: uiState.value.filterOption
        ).onEach { my ->
            when (my) {
                is BaseState.Success -> {
                    my.data.restaurantItemsData?.let {
                        _uiState.update { state ->
                            val list = it.map { restaurant -> restaurant.toUiMyListData() }
                            state.copy(
                                myList = list,
                                filterOption = if (sort == FILTER_OLD) FILTER_OLD else FILTER_NEW,
                                listPage = 2,
                                lastPage = my.data.hasNext,
                                isEmpty = list.isEmpty()
                            )
                        }
                    }
                }

                else -> _events.emit(MyRestaurantEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextPage() {
        _uiState.update { it.copy(isLoading = true) }
        getMyRestaurantListUseCase(
            limit = MyWishListViewModel.ITEM_LIMIT,
            page = uiState.value.listPage,
            sort = uiState.value.filterOption
        ).onEach { wish ->
            when (wish) {
                is BaseState.Success -> {
                    wish.data.restaurantItemsData?.let {

                        _uiState.update { state ->
                            val updateList = uiState.value.myList.toMutableList().apply {
                                addAll(it.map { restaurant ->
                                    restaurant.toUiMyListData()
                                })
                            }
                            state.copy(
                                myList = updateList,
                                listPage = uiState.value.listPage + 1,
                                lastPage = wish.data.hasNext,
                                isLoading = false
                            )
                        }
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
        deleteRestaurantUseCase(id).onEach {
            when (it) {
                is BaseState.Success -> {
                    _events.emit(MyRestaurantEvent.ShowToastMessage("삭제 되었습니다."))
                    updateList(id)
                }

                is BaseState.Error -> {
                    _events.emit(MyRestaurantEvent.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateList(deleteId: Int) {
        _uiState.update { state ->
            state.copy(
                myList = state.myList.filter { it.id != deleteId }.map { it }
            )
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val ITEM_LIMIT = 10
    }

}