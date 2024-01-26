package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class LocationData(
    val coordinates: List<Double>,
    val type: String
): BaseDomainModel
