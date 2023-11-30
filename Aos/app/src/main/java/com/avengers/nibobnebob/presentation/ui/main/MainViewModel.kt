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
    val selectedItem = MutableStateFlow(UiRestaurantData())
    val searchKeyword = MutableStateFlow("")

    fun markSearchRestaurant(item: UiRestaurantData) {
        viewModelScope.launch { selectedItem.emit(item) }
    }

    fun keepSearchKeyword(keyword : String){
        viewModelScope.launch { searchKeyword.emit(keyword) }
    }

    fun clearKeyword(){
        viewModelScope.launch { searchKeyword.emit("") }
    }


}