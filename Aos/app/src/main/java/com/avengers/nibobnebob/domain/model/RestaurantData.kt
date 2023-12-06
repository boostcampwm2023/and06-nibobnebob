package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RestaurantData(
    val hasNext : Boolean,
    val restaurantItemsData: List<RestaurantItemsData>?
) : BaseDomainModel

data class RestaurantItemsData(
    val isMy: Boolean,
    val isWish: Boolean,
    val reviewCnt: Int,
    val address: String,
    val category: String,
    val id: Int,
    val location: LocationData,
    val name: String,
    val phoneNumber: String
): BaseDomainModel
