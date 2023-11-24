package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

fun SearchRestaurantResponse.toUiRestaurantData(): UiRestaurantData = UiRestaurantData(
    id = id,
    location = location,
    name = name,
    address = address,
    phoneNumber = phoneNumber,
    reviewCount = reviewCnt,
    isInWishList = false,
    isInMyList = false

)