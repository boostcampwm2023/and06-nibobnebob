package com.avengers.nibobnebob.data.model.response

data class UserDetailResponse(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val isFollow: Boolean,
    val restaurants: List<SearchRestaurantResponse>
)

