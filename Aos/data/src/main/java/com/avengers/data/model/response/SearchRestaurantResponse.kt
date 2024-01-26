package com.avengers.data.model.response

import com.avengers.data.model.response.Location.Companion.toDomainModel
import com.avengers.domain.model.SearchRestaurantData
import com.google.gson.annotations.SerializedName

data class SearchRestaurantResponse(
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_location") val location: com.avengers.data.model.response.Location,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("restaurant_reviewImage") val restaurantImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<SearchRestaurantResponse, SearchRestaurantData> {
        override fun SearchRestaurantResponse.toDomainModel(): SearchRestaurantData =
            SearchRestaurantData(
                id = id,
                name = name,
                location = location.toDomainModel(),
                address = address,
                phoneNumber = phoneNumber,
                reviewCnt = reviewCnt,
                category = category,
                isMy = isMy,
                restaurantImage = restaurantImage
            )
    }
}
