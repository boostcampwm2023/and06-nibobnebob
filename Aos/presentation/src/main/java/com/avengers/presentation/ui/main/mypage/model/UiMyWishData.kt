package com.avengers.nibobnebob.presentation.ui.main.mypage.model

data class UiMyWishData(
    val id: Int,
    val name: String,
    val address: String,
    val isMy : Boolean,
    val isWish : Boolean = true,
)
