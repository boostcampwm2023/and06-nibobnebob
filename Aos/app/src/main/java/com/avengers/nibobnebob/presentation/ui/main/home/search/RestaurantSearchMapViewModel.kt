package com.avengers.nibobnebob.presentation.ui.main.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchMapViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {


    suspend fun updateWish(id: Int, curState: Boolean): Boolean {

        val result: Boolean = viewModelScope.async {
            var flag = true
            if (curState) {
                restaurantRepository.deleteWishRestaurant(id).onEach {
                    flag = when (it) {
                        is BaseState.Success -> true
                        else -> false
                    }
                }.launchIn(viewModelScope)
                flag
            } else {
                restaurantRepository.addWishRestaurant(id).onEach {
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
}