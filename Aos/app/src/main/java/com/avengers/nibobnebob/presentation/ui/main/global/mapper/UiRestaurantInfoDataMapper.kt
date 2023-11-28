package com.avengers.nibobnebob.presentation.ui.main.global.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiRestaurantDetailData

internal fun RestaurantDetailResponse.toUiRestaurantDetailInfo() = UiRestaurantDetailData(
    name = name,
    address = address,
    category = category,
    reviewCnt = reviewCnt,
    phoneNumber = phoneNumber,
    isWish = false //TODO : 나중에 레스토랑 세부정보 response 수정 요청 후 받아오기
)