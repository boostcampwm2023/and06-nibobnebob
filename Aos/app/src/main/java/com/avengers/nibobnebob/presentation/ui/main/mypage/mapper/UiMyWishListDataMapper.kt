package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.FilterRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

fun FilterRestaurantResponse.toMyWishListData(): UiMyWishData = UiMyWishData(
    id = id,
    name = name,
    address = address,
    isMy = isMy,
    isWish = true
)