package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val tempLocation = "37.508796 126.891074"
    private val tempRadius = "5000"


    fun searchRestaurant(keyword: CharSequence) {
        homeRepository.searchRestaurant(keyword.toString(), tempLocation, tempRadius)
            .onEach { state ->
                when (state) {
                    is BaseState.Success -> {
                        Log.d("TEST", "${state.data.body}")
                    }

                    else -> Log.d("TEST", "failed")
                }
            }.launchIn(viewModelScope)
    }
}