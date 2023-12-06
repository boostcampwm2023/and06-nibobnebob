package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class SearchRestaurantResponse(
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("isMy") val isMy: Boolean
)
