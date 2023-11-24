package com.avengers.nibobnebob.presentation.ui.main.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.data.repository.HomeRepository
import com.avengers.nibobnebob.presentation.ui.main.home.mapper.toUiSearchResultData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiSearchResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class RestaurantSearchUiState(
    val searchList : List<UiSearchResultData> = emptyList()
)

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantSearchUiState())
    val uiState : StateFlow<RestaurantSearchUiState> = _uiState.asStateFlow()

    private val tempLocation = "37.508796 126.891074"
    private val tempRadius = "5000"


    fun searchRestaurant(keyword: CharSequence) {
        homeRepository.searchRestaurant(keyword.toString(), tempLocation, tempRadius)
            .onEach { state ->
                when (state) {
                    is BaseState.Success -> {
                        _uiState.update { ui ->
                            ui.copy(
                                searchList = state.data.body.map { it.toUiSearchResultData() }
                            )
                        }
                        Log.d("TEST", "${state.data.body}")
                    }

                    else -> Log.d("TEST", "failed")
                }
            }.launchIn(viewModelScope)
    }
}