package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.LocationData
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("coordinates") val coordinates: List<Double>,
    @SerializedName("type") val type: String
): BaseDataModel {
    companion object : DomainMapper<Location, LocationData> {
        override fun Location.toDomainModel(): LocationData = LocationData(
            coordinates = coordinates,
            type = type
        )
    }
}