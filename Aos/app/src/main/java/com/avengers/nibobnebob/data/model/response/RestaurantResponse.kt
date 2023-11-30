package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    @SerializedName("isMy")val isMy: Boolean,
    @SerializedName("isWish")val isWish : Boolean,
    @SerializedName("restaurant_reviewCnt") val reviewCnt : Int,
    @SerializedName("restaurant_address")val address: String,
    @SerializedName("restaurant_category")val category: String,
    @SerializedName("restaurant_id")val id: Int,
    @SerializedName("restaurant_location")val location: Location,
    @SerializedName("restaurant_name")val name: String,
    @SerializedName("restaurant_phoneNumber")val phoneNumber : String
)