package com.avengers.nibobnebob.presentation.ui.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MyPageSharedViewModel : ViewModel() {
    private val _uiEvent = MutableSharedFlow<MyPageUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvent: SharedFlow<MyPageUiEvent> = _uiEvent.asSharedFlow()

    fun navigateToMenu(menu: Int) {
        viewModelScope.launch {
            when (menu) {
                0 -> _uiEvent.emit(MyPageUiEvent.NavigateToEditProfile)
                1 -> _uiEvent.emit(MyPageUiEvent.NavigateToMyList)
                2 -> _uiEvent.emit(MyPageUiEvent.NavigateToWishList)
                3 -> _uiEvent.emit(MyPageUiEvent.NavigateToBack)
            }

        }
    }
}