package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData

fun RestaurantResponse.toMyListData(): UiMyListData = UiMyListData(
    id = id,
    name = name,
    address = address
)