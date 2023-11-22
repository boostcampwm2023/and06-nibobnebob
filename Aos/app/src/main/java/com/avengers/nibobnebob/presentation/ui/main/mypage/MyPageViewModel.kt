package com.avengers.nibobnebob.presentation.ui.main.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.MyPageRepository
import com.avengers.nibobnebob.presentation.ui.main.mypage.mapper.toUiMyPageInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MyPageUiState(
    val nickName: String = "",
    val age: String = "",
    val location: String = "",
    val gender: String = "",
)

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        myPageRepository.getMyInfo().onEach {
            when (it) {
                is BaseState.Success -> {
                    it.data.data.toUiMyPageInfoData().apply {
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
}