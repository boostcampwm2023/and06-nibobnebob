package com.avengers.nibobnebob.presentation.ui.main.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.FollowRepository
import com.avengers.nibobnebob.presentation.ui.main.follow.mapper.toUiFollowData
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
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
    val followList: List<UiFollowData> = emptyList(),
    val recommendFollowList: List<UiFollowData> = emptyList(),
    val curListState: CurListState = CurListState.FOLLOWER
)

sealed class FollowEvents {
    data object NavigateToFollowSearch : FollowEvents()
    data class NavigateToFollowDetail(val nickName: String) : FollowEvents()
    data class ShowToastMessage(val msg: String) : FollowEvents()
    data class ShowSnackMessage(val msg: String) : FollowEvents()
}

enum class CurListState {
    FOLLOWER,
    FOLLOWING
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
                            recommendFollowList = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowSnackMessage(ERROR_MSG))
                else -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun getMyFollower() {
        _uiState.update { state ->
            state.copy(
                curListState = CurListState.FOLLOWER
            )
        }

        followRepository.getMyFollower().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followList = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowSnackMessage(ERROR_MSG))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getMyFollowing() {
        _uiState.update { state ->
            state.copy(
                curListState = CurListState.FOLLOWING
            )
        }

        followRepository.getMyFollowing().onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followList = it.data.body.map { data ->
                                data.toUiFollowData(
                                    ::follow, ::unFollow, ::navigateToFollowDetail
                                )
                            }
                        )
                    }
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowSnackMessage(ERROR_MSG))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun follow(nickName: String) {
        followRepository.follow(nickName).onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followList = when (_uiState.value.curListState) {

                                CurListState.FOLLOWER -> {
                                    _uiState.value.followList.map { data ->
                                        if (data.nickName == nickName) data.copy(isFollowing = true)
                                        else data
                                    }
                                }

                                CurListState.FOLLOWING -> {
                                    val newFollowList: MutableList<UiFollowData> = _uiState.value.followList.toMutableList()

                                    _uiState.value.recommendFollowList.forEach { data ->
                                        if (data.nickName == nickName) newFollowList.add(
                                            data.copy(
                                                isFollowing = true
                                            )
                                        )
                                    }
                                    newFollowList
                                }
                            },
                            recommendFollowList = _uiState.value.recommendFollowList.map { data ->
                                if (data.nickName == nickName) data.copy(isFollowing = true)
                                else data
                            }
                        )
                    }
                    _events.emit(FollowEvents.ShowToastMessage("팔로우 성공"))
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowSnackMessage(ERROR_MSG))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun unFollow(nickName: String) {
        followRepository.unFollow(nickName).onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            followList = when (_uiState.value.curListState) {

                                CurListState.FOLLOWER -> {
                                    _uiState.value.followList.map { data ->
                                        if (data.nickName == nickName) data.copy(isFollowing = false)
                                        else data
                                    }
                                }

                                CurListState.FOLLOWING -> {
                                    _uiState.value.followList.filter { data ->
                                        data.nickName != nickName
                                    }
                                }
                            },

                            recommendFollowList = _uiState.value.recommendFollowList.map { data ->
                                if (data.nickName == nickName) data.copy(isFollowing = false)
                                else data
                            }
                        )
                    }
                    _events.emit(FollowEvents.ShowToastMessage("언팔로우 성공"))
                }

                is BaseState.Error -> _events.emit(FollowEvents.ShowSnackMessage(ERROR_MSG))
                else -> {}
            }
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