package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

fun WishRestaurantResponse.toMyWishListData(): UiMyWishData = UiMyWishData(
    id = id,
    name = name,
    address = address,
    isMy = isMy,
    isWish = isWish
)