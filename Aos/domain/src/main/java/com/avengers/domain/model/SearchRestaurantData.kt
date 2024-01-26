package com.avengers.domain.model



import com.avengers.nibobnebob.domain.model.LocationData
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class SearchRestaurantData(
    val id: Int,
    val name: String,
    val location: LocationData,
    val address: String,
    val phoneNumber: String,
    val reviewCnt: Int,
    val category: String,
    val isMy: Boolean,
    val restaurantImage: String
) : BaseDomainModel
