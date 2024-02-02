package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class MyRestaurantData(
    val hasNext: Boolean,
    val restaurantItemsData: List<MyRestaurantItemData>?
) : BaseDomainModel

data class MyRestaurantItemData(
    val isMy: Boolean,
    val isWish: Boolean,
    val reviewCnt: Int,
    val address: String,
    val category: String,
    val id: Int,
    val createdAt: String?,
    val location: LocationData,
    val name: String,
    val phoneNumber: String,
    val restaurantImage: String
) : BaseDomainModel
