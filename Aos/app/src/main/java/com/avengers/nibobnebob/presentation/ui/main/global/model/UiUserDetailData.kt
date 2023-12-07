package com.avengers.nibobnebob.presentation.ui.main.global.model

data class UiUserDetailData(
    val nickName: String = "",
    val region: String = "",
    val age: String = "",
    val isFollow: Boolean = false,
    val profileImage: String = "",
    val restaurants: List<UiUserDetailRestaurantData> = emptyList()
)


data class UiUserDetailRestaurantData(
    val name: String,
    val address: String,
    val phoneNumber: String
)
