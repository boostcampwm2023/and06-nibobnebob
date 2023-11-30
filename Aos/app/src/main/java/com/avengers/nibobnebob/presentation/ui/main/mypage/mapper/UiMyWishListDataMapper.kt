package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

fun RestaurantResponse.toMyWishListData(): UiMyWishData = UiMyWishData(
    id = id,
    name = name,
    address = address,
    isMy = isMy,
    isWish = true
)