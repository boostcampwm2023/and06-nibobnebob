package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.google.gson.annotations.SerializedName

data class RestaurantIsWishResponse(
    @SerializedName("isWish") val isWish: Boolean
) : BaseDataModel {
    companion object : DomainMapper<RestaurantIsWishResponse, RestaurantIsWishData> {
        override fun RestaurantIsWishResponse.toDomainModel(): RestaurantIsWishData =
            RestaurantIsWishData(
                isWish = isWish
            )
    }
}
