package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.google.gson.annotations.SerializedName

data class WishRestaurantResponse(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String
) : BaseDataModel {
    companion object : DomainMapper<WishRestaurantResponse, WishRestaurantData> {
        override fun WishRestaurantResponse.toDomainModel(): WishRestaurantData =
            WishRestaurantData(
                isMy = isMy,
                isWish = isWish,
                address = address,
                category = category,
                id = id,
                location = location,
                name = name,
                phoneNumber = phoneNumber
            )
    }
}