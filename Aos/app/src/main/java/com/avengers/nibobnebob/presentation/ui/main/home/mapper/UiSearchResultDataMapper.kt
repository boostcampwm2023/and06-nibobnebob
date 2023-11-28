package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

fun SearchRestaurantResponse.toUiRestaurantData(): UiRestaurantData = UiRestaurantData(
    id = id,
    latitude = location.split(" ")[0].toDouble(),
    longitude = location.split(" ")[1].toDouble(),
    name = name,
    address = address,
    phoneNumber = phoneNumber,
    reviewCount = reviewCnt.toString(),
    isInWishList = false,
    isInMyList = false
)