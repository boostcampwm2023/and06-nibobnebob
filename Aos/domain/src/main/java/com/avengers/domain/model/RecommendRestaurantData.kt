package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RecommendRestaurantData(
    val category: String,
    val id: Int,
    val name: String,
    val reviewImage: String
) : BaseDomainModel
