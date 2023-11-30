package com.avengers.nibobnebob.presentation.ui.main.global.mapper

import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiRestaurantDetailData

internal fun RestaurantDetailResponse.toUiRestaurantDetailInfo(
    onWishClick : () -> Unit
) : UiRestaurantDetailData{
    val formattedPhoneNumber = phoneNumber.ifEmpty { "전화번호 없음" }
    val formattedReviewCnt = "$reviewCnt 개"

    return UiRestaurantDetailData(
        name = name,
        address = address,
        category = category,
        reviewCnt = formattedReviewCnt,
        phoneNumber = formattedPhoneNumber,
        isWish = isWish,
        isMy = isMy,
        onWishClick = onWishClick
    )
}