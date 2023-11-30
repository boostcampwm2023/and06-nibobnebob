package com.avengers.nibobnebob.presentation.ui.main.global.userdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.FollowRepository
import com.avengers.nibobnebob.presentation.ui.main.global.mapper.toUiUserDetailData
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiUserDetailData
import com.avengers.nibobnebob.presentation.util.Constants
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


data class UserDetailUiState(
    val userDetail: UiUserDetailData = UiUserDetailData()
)

sealed class UserDetailEvents {
    data class ShowSnackMessage(val msg: String) : UserDetailEvents()
    data class ShowToastMessage(val msg: String) : UserDetailEvents()
    data object NavigateToBack : UserDetailEvents()
}

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val followRepository: FollowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailUiState())
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UserDetailEvents>()
    val events: SharedFlow<UserDetailEvents> = _events.asSharedFlow()

    private var nickName = ""

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserDetail() {
        followRepository.getUserDetail(nickName).onEach {

            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            userDetail = it.data.body.toUiUserDetailData()
                        )
                    }
                }

                is BaseState.Error -> {
                    _events.emit(UserDetailEvents.ShowSnackMessage(it.message))
                }
            }

        }.launchIn(viewModelScope)
    }

    fun tryFollowUnFollow(){
        if(_uiState.value.userDetail.isFollow) unFollow()
        else follow()
    }

    private fun follow() {
        followRepository.follow(nickName).onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            userDetail = _uiState.value.userDetail.copy(isFollow = true)
                        )
                    }
                    _events.emit(UserDetailEvents.ShowToastMessage("팔로우 성공"))
                }

                is BaseState.Error -> _events.emit(UserDetailEvents.ShowSnackMessage(Constants.ERROR_MSG))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun unFollow() {
        followRepository.unFollow(nickName).onEach {
            when (it) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            userDetail = _uiState.value.userDetail.copy(isFollow = false)
                        )
                    }
                    _events.emit(UserDetailEvents.ShowToastMessage("언팔로우 성공"))
                }

                is BaseState.Error -> _events.emit(UserDetailEvents.ShowSnackMessage(Constants.ERROR_MSG))
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun setNick(data: String) {
        nickName = data
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(UserDetailEvents.NavigateToBack)
        }
    }
}