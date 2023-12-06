package com.avengers.nibobnebob.domain.model


import com.avengers.nibobnebob.data.model.response.Location
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class SearchRestaurantData(
    val id: Int,
    val name: String,
    val location: Location,
    val address: String,
    val phoneNumber: String,
    val reviewCnt: Int,
    val category: String,
    val isMy: Boolean
) : BaseDomainModel
