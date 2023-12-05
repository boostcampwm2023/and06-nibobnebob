package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

fun WishRestaurantData.toMyWishListData(): UiMyWishData = UiMyWishData(
    id = id,
    name = name,
    address = address,
    isMy = isMy,
    isWish = isWish
)