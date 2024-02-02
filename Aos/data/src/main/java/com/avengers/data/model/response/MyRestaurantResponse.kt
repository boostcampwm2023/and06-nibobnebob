package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.data.model.response.Location.Companion.toDomainModel
import com.avengers.data.model.response.MyRestaurantItem.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyRestaurantData
import com.avengers.nibobnebob.domain.model.MyRestaurantItemData
import com.google.gson.annotations.SerializedName

data class MyRestaurantResponse(
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("items") val restaurantItems: List<com.avengers.data.model.response.MyRestaurantItem>?
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.MyRestaurantResponse, MyRestaurantData> {
        override fun com.avengers.data.model.response.MyRestaurantResponse.toDomainModel(): MyRestaurantData = MyRestaurantData(
            hasNext = hasNext,
            restaurantItemsData = restaurantItems?.map { it.toDomainModel() }
        )
    }
}

data class MyRestaurantItem(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("restaurant_location") val location: com.avengers.data.model.response.Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewImage") val restaurantImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.MyRestaurantItem, MyRestaurantItemData> {
        override fun com.avengers.data.model.response.MyRestaurantItem.toDomainModel(): MyRestaurantItemData = MyRestaurantItemData(
            isMy = isMy,
            isWish = isWish,
            reviewCnt = reviewCnt,
            address = address,
            category = category,
            id = id,
            createdAt = createdAt,
            location = location.toDomainModel(),
            name = name,
            phoneNumber = phoneNumber,
            restaurantImage = restaurantImage
        )
    }
}


