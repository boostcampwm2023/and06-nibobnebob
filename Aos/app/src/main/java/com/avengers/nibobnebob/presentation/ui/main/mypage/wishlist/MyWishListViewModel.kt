package com.avengers.nibobnebob.presentation.ui.main.mypage.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteMyWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.GetMyWishListUseCase
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toMyWishListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData
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

data class MyWishUiState(
    val wishList: List<UiMyWishData> = emptyList(),
    val filterOption: String = "",
    val listPage: Int = 1,
    val lastPage: Boolean = false,
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
)

sealed class MyWishEvent {
    data class NavigateToRestaurantDetail(val id: Int) : MyWishEvent()
    data class NavigateToRestaurantAdd(val item: UiMyWishData) : MyWishEvent()
    data class ShowSnackMessage(val msg: String) : MyWishEvent()
    data class ShowToastMessage(val msg: String) : MyWishEvent()
}

@HiltViewModel
class MyWishListViewModel @Inject constructor(
    private val getMyWishListUseCase: GetMyWishListUseCase,
    private val deleteMyWishRestaurantUseCase: DeleteMyWishRestaurantUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyWishUiState())
    val uiState: StateFlow<MyWishUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyWishEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<MyWishEvent> = _events.asSharedFlow()


    fun myWishList(
        sort: String? = null,
    ) {
        getMyWishListUseCase(
            limit = ITEM_LIMIT, page = FIRST_PAGE, sort = sort
                ?: uiState.value.filterOption
        ).onEach { wish ->
            when (wish) {
                is BaseState.Success -> {
                    wish.data.wishRestaurantItemsData?.let {
                        _uiState.update { state ->
                            val wishList = it.map { restaurant ->
                                restaurant.toMyWishListData()
                            }
                            state.copy(
                                wishList = wishList,
                                filterOption = if (sort == FILTER_OLD) FILTER_OLD else FILTER_NEW,
                                listPage = 2,
                                lastPage = wish.data.hasNext,
                                isEmpty = wishList.isEmpty()
                            )
                        }
                    }
                }

                else -> _events.emit(MyWishEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }

    fun loadNextPage() {
        _uiState.update { it.copy(isLoading = true) }
        getMyWishListUseCase(
            limit = ITEM_LIMIT, page = uiState.value.listPage, sort = uiState.value.filterOption
        ).onEach { wish ->
            when (wish) {
                is BaseState.Success -> {
                    wish.data.wishRestaurantItemsData?.let {

                        _uiState.update { state ->
                            val updateList = uiState.value.wishList.toMutableList().apply {
                                addAll(it.map { restaurant ->
                                    restaurant.toMyWishListData()
                                })
                            }
                            state.copy(
                                wishList = updateList,
                                listPage = uiState.value.listPage + 1,
                                lastPage = wish.data.hasNext,
                                isLoading = false
                            )
                        }
                    }
                }

                else -> _events.emit(MyWishEvent.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }


    fun showDetail(id: Int) {
        viewModelScope.launch { _events.emit(MyWishEvent.NavigateToRestaurantDetail(id)) }
    }

    fun addMyList(item: UiMyWishData) {
        viewModelScope.launch {
            _events.emit(
                if (item.isMy) MyWishEvent.ShowToastMessage("이미 나의 맛집 리스트에 추가된 맛집입니다.")
                else MyWishEvent.NavigateToRestaurantAdd(item)
            )
        }
    }

    fun deleteWishList(id: Int) {
        deleteMyWishRestaurantUseCase(id).onEach {
            when (it) {
                is BaseState.Success -> {
                    _events.emit(MyWishEvent.ShowToastMessage("삭제 되었습니다."))
                    updateList(id)
                }

                is BaseState.Error -> {
                    _events.emit(MyWishEvent.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun updateList(deleteId: Int) {
        _uiState.update { state ->
            state.copy(
                wishList = state.wishList.filter { it.id != deleteId }.map { it }
            )
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val ITEM_LIMIT = 10
    }

}