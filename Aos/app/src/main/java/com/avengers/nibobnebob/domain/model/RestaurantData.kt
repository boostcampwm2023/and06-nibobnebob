package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.data.model.response.Location
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RestaurantData(
    val hasNext: Boolean,
    val restaurantItemsData: List<RestaurantItemsData>?
) : BaseDomainModel

data class RestaurantItemsData(
    val isMy: Boolean,
    val isWish: Boolean,
    val reviewCnt: Int,
    val address: String,
    val category: String,
    val id: Int,
    val createdAt: String,
    val location: Location,
    val name: String,
    val phoneNumber: String
) : BaseDomainModel
