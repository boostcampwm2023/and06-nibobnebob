package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.RecommendRestaurantData
import com.google.gson.annotations.SerializedName

data class RecommendRestaurantResponse(
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_reviewImage") val reviewImage: String
) : BaseDataModel {
    companion object : DomainMapper<RecommendRestaurantResponse, RecommendRestaurantData> {
        override fun RecommendRestaurantResponse.toDomainModel() = RecommendRestaurantData(
            category = category,
            id = id,
            name = name,
            reviewImage = reviewImage
        )
    }
}