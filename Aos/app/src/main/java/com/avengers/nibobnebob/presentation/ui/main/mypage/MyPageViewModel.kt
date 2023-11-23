package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.MyPageRepository
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toUiMyPageInfoData
import com.avengers.nibobnebob.presentation.util.dialogManager
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

data class MyPageUiState(
    val nickName: String = "",
    val age: String = "",
    val location: String = "",
    val gender: String = "",
)

sealed class MyEditPageEvent{
    data object NavigateToIntro : MyEditPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyEditPageEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events : SharedFlow<MyEditPageEvent> = _events.asSharedFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        myPageRepository.getMyInfo().onEach {
            when (it) {
                is BaseState.Success -> {
                    it.data.body.toUiMyPageInfoData().apply {
                        _uiState.update { state ->
                            state.copy(
                                nickName = nickName,
                                age = age,
                                location = location,
                                gender = gender
                            )
                        }
                    }
                }

                is BaseState.Error -> Log.d("TEST", "에러")
            }

        }.launchIn(viewModelScope)

    }

    fun logout(){

        viewModelScope.launch {
            dataStoreManager.deleteAccessToken()
            dataStoreManager.deleteRefreshToken()
            _events.emit(MyEditPageEvent.NavigateToIntro)
        }

//        myPageRepository.logout().onEach {
//            when(it){
//                is BaseState.Success -> {
//                    dataStoreManager.deleteAccessToken()
//                    dataStoreManager.deleteRefreshToken()
//                    _events.emit(MyEditPageEvent.NavigateToIntro)
//                }
//                else -> Log.d("TEST", "로그아웃 실패")
//            }
//        }.launchIn(viewModelScope)
    }

    fun withdraw(){
        myPageRepository.withdraw().onEach {
            when(it){
                is BaseState.Success -> {
                    dataStoreManager.deleteAccessToken()
                    dataStoreManager.deleteRefreshToken()
                    _events.emit(MyEditPageEvent.NavigateToIntro)
                }
                else -> Log.d("TEST", "탈퇴 실패")
            }
        }.launchIn(viewModelScope)
    }
}