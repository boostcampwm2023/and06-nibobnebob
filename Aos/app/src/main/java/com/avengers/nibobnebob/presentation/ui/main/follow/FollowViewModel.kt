package com.avengers.nibobnebob.presentation.ui.main.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FollowUiState(
    val followingList: List<UiFollowData> = emptyList(),
    val followList: List<UiFollowData> = emptyList(),
    val recommendFriend: List<UiFollowData> = emptyList()
)

sealed class FollowEvents{
    data object NavigateToFollowSearch: FollowEvents()
    data class NavigateToFollowDetail(val nickName: String): FollowEvents()
}


@HiltViewModel
class FollowViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(FollowUiState())
    val uiState:StateFlow<FollowUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FollowEvents>()
    val events: SharedFlow<FollowEvents> = _events.asSharedFlow()

    fun navigateToFollowSearch(){
        viewModelScope.launch {
            _events.emit(FollowEvents.NavigateToFollowSearch)
        }
    }

    fun navigateToFollowDetail(nickName: String){
        viewModelScope.launch {
            _events.emit(FollowEvents.NavigateToFollowDetail(nickName))
        }
    }

}