package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class FilterRestaurantResponse(
    //TODO : 여기 서버 어떻게 올지 대기중 ismy -> isMy
    @SerializedName("ismy")val isMy: Boolean,
//    @SerializedName("restaurant_reviewCnt") val reviewCnt : Int,
    @SerializedName("restaurant_address")val address: String,
    @SerializedName("restaurant_category")val category: String,
    @SerializedName("restaurant_id")val id: Int,
    @SerializedName("restaurant_location")val location: Location,
    @SerializedName("restaurant_name")val name: String,
    @SerializedName("restaurant_phoneNumber")val phoneNumber : String
)