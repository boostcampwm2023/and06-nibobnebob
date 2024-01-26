package com.avengers.data.model.response

import com.avengers.data.model.response.Location.Companion.toDomainModel
import com.avengers.data.model.response.UserDetailRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.UserDetailData
import com.avengers.nibobnebob.domain.model.UserDetailRestaurantData
import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val isFollow: Boolean,
    val restaurants: List<UserDetailRestaurantResponse>,
    val profileImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<UserDetailResponse, UserDetailData> {
        override fun UserDetailResponse.toDomainModel(): UserDetailData = UserDetailData(
            nickName = nickName,
            birthdate = birthdate,
            region = region,
            isMale = isMale,
            isFollow = isFollow,
            restaurants = restaurants.map { it.toDomainModel() },
            profileImage = profileImage
        )
    }
}


data class UserDetailRestaurantResponse(
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_location") val location: com.avengers.data.model.response.Location,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("isMy") val isMy: Boolean
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<UserDetailRestaurantResponse, UserDetailRestaurantData> {
        override fun UserDetailRestaurantResponse.toDomainModel(): UserDetailRestaurantData =
            UserDetailRestaurantData(
                id = id,
                name = name,
                location = location.toDomainModel(),
                address = address,
                phoneNumber = phoneNumber,
                reviewCnt = reviewCnt,
                category = category,
                isMy = isMy
            )
    }
}

