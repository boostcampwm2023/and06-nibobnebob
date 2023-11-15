package com.avengers.nibobnebob.presentation.ui.main.mypage

sealed interface MyPageUiEvent {
    data object NavigateToEditProfile : MyPageUiEvent
    data object NavigateToMyList : MyPageUiEvent
    data object NavigateToWishList : MyPageUiEvent
    data object NavigateToBack : MyPageUiEvent

}