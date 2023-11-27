package com.avengers.nibobnebob.presentation.ui.main.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.FollowRepository
import com.avengers.nibobnebob.presentation.ui.main.follow.mapper.toUiFollowData
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData
import dagger.hilt.android.lifecycle.HiltViewModel
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

data class FollowUiState(
    val followingList: List<UiFollowData> = emptyList(),
    val followerList: List<UiFollowData> = emptyList(),
    val recommendFriend: List<UiFollowData> = emptyList()
)

sealed class FollowEvents {
    data object NavigateToFollowSearch : FollowEvents()
    data class NavigateToFollowDetail(val nickName: String) : FollowEvents()
    data class ShowToastMessage(val msg: String) : FollowEvents()
}


@HiltViewModel
class FollowViewModel @Inject constructor(
    private val followRepository: FollowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FollowUiState())
    val uiState: StateFlow<FollowUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FollowEvents>()
    val events: SharedFlow<FollowEvents> = _events.asSharedFlow()

    fun getMyRecommendFollow() {
        followRepository.getMyRecommendFollow().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            recommendFriend = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowToastMessage(it.message))
                else -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun getMyFollower() {
        followRepository.getMyFollower().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followerList = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowToastMessage(it.message))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getMyFollowing() {
        followRepository.getMyFollowing().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followingList = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowToastMessage(it.message))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun follow(nickName: String) {
        followRepository.follow(nickName).onEach {

        }.launchIn(viewModelScope)
    }

    private fun unFollow(nickName: String) {
        followRepository.unFollow(nickName).onEach {

        }.launchIn(viewModelScope)
    }


    fun navigateToFollowSearch() {
        viewModelScope.launch {
            _events.emit(FollowEvents.NavigateToFollowSearch)
        }
    }

    private fun navigateToFollowDetail(nickName: String) {
        viewModelScope.launch {
            _events.emit(FollowEvents.NavigateToFollowDetail(nickName))
        }
    }

}