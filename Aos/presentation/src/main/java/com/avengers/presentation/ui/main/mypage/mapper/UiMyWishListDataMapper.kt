package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.domain.model.WishRestaurantItemsData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

fun WishRestaurantItemsData.toMyWishListData(): UiMyWishData = UiMyWishData(
    id = id,
    name = name,
    address = address,
    isMy = isMy,
    isWish = isWish
)