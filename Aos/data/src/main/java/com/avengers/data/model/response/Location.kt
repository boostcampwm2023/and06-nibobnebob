package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.LocationData
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("coordinates") val coordinates: List<Double>,
    @SerializedName("type") val type: String
): com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.Location, LocationData> {
        override fun com.avengers.data.model.response.Location.toDomainModel(): LocationData = LocationData(
            coordinates = coordinates,
            type = type
        )
    }
}