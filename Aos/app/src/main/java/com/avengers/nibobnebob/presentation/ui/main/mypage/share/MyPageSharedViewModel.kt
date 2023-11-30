package com.avengers.nibobnebob.presentation.ui.main.mypage.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


sealed class MyPageSharedUiEvent {
    data object NavigateToEditProfile : MyPageSharedUiEvent()
    data object NavigateToMyList : MyPageSharedUiEvent()
    data object NavigateToWishList : MyPageSharedUiEvent()
    data object NavigateToBack : MyPageSharedUiEvent()

}

class MyPageSharedViewModel : ViewModel() {
    private val _uiEvent = MutableSharedFlow<MyPageSharedUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvent: SharedFlow<MyPageSharedUiEvent> = _uiEvent.asSharedFlow()

    fun navigateToMenu(menu: Int) {
        viewModelScope.launch {
            when (menu) {
                0 -> _uiEvent.emit(MyPageSharedUiEvent.NavigateToEditProfile)
                1 -> _uiEvent.emit(MyPageSharedUiEvent.NavigateToMyList)
                2 -> _uiEvent.emit(MyPageSharedUiEvent.NavigateToWishList)
                3 -> _uiEvent.emit(MyPageSharedUiEvent.NavigateToBack)
            }

        }
    }
}