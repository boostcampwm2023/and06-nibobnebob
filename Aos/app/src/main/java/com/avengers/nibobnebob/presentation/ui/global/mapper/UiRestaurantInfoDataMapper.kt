package com.avengers.nibobnebob.presentation.ui.global.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.presentation.ui.global.model.UiRestaurantDetailData

internal fun RestaurantDetailResponse.toUiRestaurantDetailInfo() = UiRestaurantDetailData(
    name = name,
    address = address,
    category = category,
    reviewCnt = reviewCnt,
    phoneNumber = phoneNumber
)