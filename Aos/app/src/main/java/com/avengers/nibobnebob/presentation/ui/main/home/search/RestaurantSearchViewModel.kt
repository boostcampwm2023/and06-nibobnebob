package com.avengers.nibobnebob.presentation.ui.main.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.HomeRepository
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
        val index: Int
    ) : RestaurantSearchEvent()
}

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantSearchUiState())
    val uiState: StateFlow<RestaurantSearchUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantSearchEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<RestaurantSearchEvent> = _events.asSharedFlow()

    private val tempLocation = "37.508796 126.891074"
    private val tempRadius = "5000"


    fun searchRestaurant(keyword: CharSequence) {
        if (keyword.isBlank()) {
            _uiState.update { ui -> ui.copy(searchList = emptyList(), searchKeyword = "", isResultEmpty = true) }
            return
        }
        homeRepository.searchRestaurant(keyword.toString(), tempLocation, tempRadius)
            .onEach { state ->
                when (state) {
                    is BaseState.Success -> {
                        val item = state.data.body
                        _uiState.update { ui ->
                            ui.copy(
                                searchList = item.map { it.toUiRestaurantData() },
                                searchKeyword = keyword.toString(),
                                isResultEmpty = item.isEmpty()
                            )
                        }
                    }

                    else -> {
                        _uiState.update { ui -> ui.copy(
                            searchList = emptyList(),
                            searchKeyword = "",
                            isResultEmpty = true
                        ) }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun onClickSearchItem(index: Int) {
        viewModelScope.launch {
            _events.emit(RestaurantSearchEvent.OnClickResultItem(index))
        }
    }
}