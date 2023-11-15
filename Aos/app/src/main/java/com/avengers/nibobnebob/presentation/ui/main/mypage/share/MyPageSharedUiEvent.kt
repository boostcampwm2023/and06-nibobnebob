package com.avengers.nibobnebob.presentation.ui.main.mypage.share

sealed interface MyPageSharedUiEvent {
    data object NavigateToEditProfile : MyPageSharedUiEvent
    data object NavigateToMyList : MyPageSharedUiEvent
    data object NavigateToWishList : MyPageSharedUiEvent
    data object NavigateToBack : MyPageSharedUiEvent

}