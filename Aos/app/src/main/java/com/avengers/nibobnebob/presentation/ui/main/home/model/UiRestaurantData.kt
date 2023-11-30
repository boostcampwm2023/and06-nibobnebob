package com.avengers.nibobnebob.presentation.ui.main.home.model

data class UiRestaurantData(
    val id: Int = -1,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val reviewCount: String = "",
    val isInWishList: Boolean = false,
    val isInMyList: Boolean = false
)