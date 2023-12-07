package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.domain.model.MyRestaurantItemData
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData


//TODO : 위에 꺼 HOME까지 건드리는 것 같아서 따로 빼서 했어!! (나중에 합치자)
fun RestaurantItemsData.toUiRestaurantData(): UiRestaurantData {
    val formattedPhoneNumber = phoneNumber.ifEmpty { "전화번호 없음" }
    val formattedReviewCnt = "$reviewCnt 개"

    return UiRestaurantData(
        id = id,
        latitude = location.coordinates[1], //응답 형식이 "경도 위도"임..
        longitude = location.coordinates[0],
        name = name,
        address = address,
        phoneNumber = formattedPhoneNumber,
        reviewCount = formattedReviewCnt,
        isInWishList = isWish,
        isInMyList = isMy
    )
}

fun MyRestaurantItemData.toUiRestaurantData(): UiRestaurantData {
    val formattedPhoneNumber = phoneNumber.ifEmpty { "전화번호 없음" }
    val formattedReviewCnt = "$reviewCnt 개"

    return UiRestaurantData(
        id = id,
        latitude = location.coordinates[1], //응답 형식이 "경도 위도"임..
        longitude = location.coordinates[0],
        name = name,
        address = address,
        phoneNumber = formattedPhoneNumber,
        reviewCount = formattedReviewCnt,
        isInWishList = isWish,
        isInMyList = isMy
    )
}
