package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.google.gson.annotations.SerializedName

data class RestaurantIsWishResponse(
    @SerializedName("isWish") val isWish: Boolean
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<RestaurantIsWishResponse, RestaurantIsWishData> {
        override fun RestaurantIsWishResponse.toDomainModel(): RestaurantIsWishData =
            RestaurantIsWishData(
                isWish = isWish
            )
    }
}
