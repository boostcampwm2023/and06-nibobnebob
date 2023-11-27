package com.avengers.nibobnebob.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager
) : BaseActivityViewModel(networkManager) {
    private val _uiState = MutableStateFlow(UiRestaurantData())
    val uiState: StateFlow<UiRestaurantData> = _uiState.asStateFlow()

    fun markSearchRestaurant(item: UiRestaurantData) {
        viewModelScope.launch { _uiState.emit(item) }
    }


}