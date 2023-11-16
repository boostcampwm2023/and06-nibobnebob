package com.avengers.nibobnebob.presentation.ui.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        getUserInfo()
    }

    data class User(
        val nickName: String,
        val age: String,
        val area: String
    )

    private fun getUserInfo() {
        // data layer 에서 가져왔다고 가정
        val user = User("tester", "20대", "용산구")
        flow { emit(user) }.onEach {
            _uiState.update { state ->
                state.copy(nickName = it.nickName, age = it.age, area = it.area)
            }
        }.launchIn(viewModelScope)

    }
}