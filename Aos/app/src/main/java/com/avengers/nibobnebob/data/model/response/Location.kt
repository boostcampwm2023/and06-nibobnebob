package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("coordinates") val coordinates: List<Double>,
    @SerializedName("type") val type: String
)