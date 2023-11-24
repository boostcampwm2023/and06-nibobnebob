package com.avengers.nibobnebob.presentation.ui.main.home.model

data class UiRestaurantData(
    val id: Long = -1,
    val location: String = "",
    val name: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val reviewCount: Int = 0,
    val isInWishList: Boolean = false,
    val isInMyList: Boolean = false
)