package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.Location.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.google.gson.annotations.SerializedName


data class RestaurantItemResponse(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewImage") val restaurantImage: String
) : BaseDataModel {
    companion object : DomainMapper<RestaurantItemResponse, RestaurantItemsData> {
        override fun RestaurantItemResponse.toDomainModel(): RestaurantItemsData = RestaurantItemsData(
            isMy = isMy,
            isWish = isWish,
            reviewCnt = reviewCnt,
            address = address,
            category = category,
            id = id,
            location = location.toDomainModel(),
            name = name,
            phoneNumber = phoneNumber,
            restaurantImage = restaurantImage
        )
    }
}


