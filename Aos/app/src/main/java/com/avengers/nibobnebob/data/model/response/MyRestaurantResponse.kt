package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.Location.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.MyRestaurantItem.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyRestaurantData
import com.avengers.nibobnebob.domain.model.MyRestaurantItemData
import com.google.gson.annotations.SerializedName

data class MyRestaurantResponse(
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("items") val restaurantItems: List<MyRestaurantItem>?
) : BaseDataModel {
    companion object : DomainMapper<MyRestaurantResponse, MyRestaurantData> {
        override fun MyRestaurantResponse.toDomainModel(): MyRestaurantData = MyRestaurantData(
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
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String
) : BaseDataModel {
    companion object : DomainMapper<MyRestaurantItem, MyRestaurantItemData> {
        override fun MyRestaurantItem.toDomainModel(): MyRestaurantItemData = MyRestaurantItemData(
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


