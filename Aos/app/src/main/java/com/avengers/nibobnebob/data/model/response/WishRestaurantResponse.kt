package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.Location.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.WishRestaurantItems.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.WishRestaurantItemsData
import com.google.gson.annotations.SerializedName

data class WishRestaurantResponse(
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("items") val wishRestaurantItems: List<WishRestaurantItems>?
) : BaseDataModel {
    companion object : DomainMapper<WishRestaurantResponse, WishRestaurantData> {
        override fun WishRestaurantResponse.toDomainModel(): WishRestaurantData =
            WishRestaurantData(
                hasNext = hasNext,
                wishRestaurantItemsData = wishRestaurantItems?.map { it.toDomainModel() }
            )
    }
}

data class WishRestaurantItems(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String
) : BaseDataModel {
    companion object : DomainMapper<WishRestaurantItems, WishRestaurantItemsData> {
        override fun WishRestaurantItems.toDomainModel(): WishRestaurantItemsData =
            WishRestaurantItemsData(
                isMy = isMy,
                isWish = isWish,
                address = address,
                category = category,
                id = id,
                location = location.toDomainModel(),
                name = name,
                phoneNumber = phoneNumber
            )
    }
}
