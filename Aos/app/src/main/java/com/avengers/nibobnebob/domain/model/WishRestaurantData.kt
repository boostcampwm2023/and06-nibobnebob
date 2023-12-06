package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class WishRestaurantData(
    val hasNext : Boolean,
    val wishRestaurantItemsData: List<WishRestaurantItemsData>?
) : BaseDomainModel

data class WishRestaurantItemsData(
    val isMy: Boolean,
    val isWish: Boolean,
    val address: String,
    val category: String,
    val id: Int,
    val location: LocationData,
    val name: String,
    val phoneNumber: String
): BaseDomainModel
