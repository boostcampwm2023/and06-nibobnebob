package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.RestaurantItems.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("items") val restaurantItems: List<RestaurantItems>?
) : BaseDataModel {
    companion object : DomainMapper<RestaurantResponse, RestaurantData> {
        override fun RestaurantResponse.toDomainModel(): RestaurantData = RestaurantData(
            hasNext = hasNext,
            restaurantItemsData = restaurantItems?.map { it.toDomainModel() }
        )
    }
}

data class RestaurantItems(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String
) : BaseDataModel {
    companion object : DomainMapper<RestaurantItems, RestaurantItemsData> {
        override fun RestaurantItems.toDomainModel(): RestaurantItemsData = RestaurantItemsData(
            isMy = isMy,
            isWish = isWish,
            reviewCnt = reviewCnt,
            address = address,
            category = category,
            id = id,
            createdAt = createdAt,
            location = location.toDomainModel(),
            name = name,
            phoneNumber = phoneNumber
        )
    }
}


