package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.data.model.response.Location
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RestaurantData(
    val isMy: Boolean,
    val isWish: Boolean,
    val reviewCnt: Int,
    val address: String,
    val category: String,
    val id: Int,
    val location: Location,
    val name: String,
    val phoneNumber: String
) : BaseDomainModel
