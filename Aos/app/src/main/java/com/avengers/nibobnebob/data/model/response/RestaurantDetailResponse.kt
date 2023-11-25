package com.avengers.nibobnebob.data.model.response

data class RestaurantDetailResponse(
    val address: String,
    val category: String,
    val id: Int,
    val location: Location,
    val name: String,
    val phoneNumber: String,
    val reviewCnt: Int
)
