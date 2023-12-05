package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

fun RestaurantResponse.toUiRestaurantData(): UiRestaurantData = UiRestaurantData(
    id = id,
    latitude = location.coordinates[1], //응답 형식이 "경도 위도"임..
    longitude = location.coordinates[0],
    name = name,
    address = address,
    phoneNumber = phoneNumber,
    reviewCount = "${reviewCnt}개",
    isInWishList = isWish,
    isInMyList = isMy
)