package com.avengers.nibobnebob.presentation.ui.main.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.AddWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteMyWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.GetRestaurantIsWishUseCase
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

enum class WishStatus {
    INIT, CHANGED, SAME
}

sealed class SearchMapEvents {
    data class ShowSnackMessage(
        val msg: String
    ) : SearchMapEvents()
}

@HiltViewModel
class RestaurantSearchMapViewModel @Inject constructor(
    private val deleteMyWishRestaurantUseCase: DeleteMyWishRestaurantUseCase,
    private val addWishRestaurantUseCase: AddWishRestaurantUseCase,
    private val getRestaurantIsWishUseCase: GetRestaurantIsWishUseCase
) : ViewModel() {

    val wishChanged = MutableStateFlow(WishStatus.INIT)

    private val _events = MutableSharedFlow<SearchMapEvents>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )
    val events: SharedFlow<SearchMapEvents> = _events.asSharedFlow()


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

        return result
    }

    fun getRestaurantIsWish(id: Int, inMyWish: Boolean) {
        getRestaurantIsWishUseCase(id).onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    wishChanged.emit(if (inMyWish != state.data.isWish) WishStatus.CHANGED else WishStatus.SAME)
                }

                else -> _events.emit(SearchMapEvents.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }
}