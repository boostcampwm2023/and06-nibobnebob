package com.avengers.nibobnebob.presentation.ui.main.global.model

data class UiRestaurantDetailData(
    val name: String,
    val address: String,
    val category: String,
    val reviewCnt: String,
    val phoneNumber: String,
    val isWish: Boolean,
    val isMy: Boolean,
    val onWishClick: () -> Unit
)