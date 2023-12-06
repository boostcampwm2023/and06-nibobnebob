package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData

fun RestaurantItemsData.toMyListData(): UiMyListData = UiMyListData(
    id = id,
    name = name,
    address = address
)