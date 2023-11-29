package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.FilterRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageListData

fun FilterRestaurantResponse.toMyPageListData(): UiMyPageListData = UiMyPageListData(
    id = id,
    name = name,
    address = address
)