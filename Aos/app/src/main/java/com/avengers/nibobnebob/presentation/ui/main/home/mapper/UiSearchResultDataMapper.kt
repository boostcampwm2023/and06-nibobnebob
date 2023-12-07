package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.domain.model.SearchRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

fun SearchRestaurantData.toUiRestaurantData(): UiRestaurantData = UiRestaurantData(
    id = id,
    latitude = location.coordinates[1],
    longitude = location.coordinates[0],
    name = name,
    address = address,
    phoneNumber = phoneNumber,
    reviewCount = reviewCnt.toString(),
    isInWishList = false,
    isInMyList = false,
    reviewImage = restaurantImage

)