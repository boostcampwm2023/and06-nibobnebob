package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData

fun RestaurantData.toMyListData(): UiMyListData = UiMyListData(
    id = id,
    name = name,
    address = address
)