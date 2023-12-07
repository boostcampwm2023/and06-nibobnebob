package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.UserDetailData

data class UserDetailResponse(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val isFollow: Boolean,
    val restaurants: List<SearchRestaurantResponse>
) : BaseDataModel {
    companion object : DomainMapper<UserDetailResponse, UserDetailData> {
        override fun UserDetailResponse.toDomainModel(): UserDetailData = UserDetailData(
            nickName = nickName,
            birthdate = birthdate,
            region = region,
            isMale = isMale,
            isFollow = isFollow,
            restaurants = restaurants.map { it.toDomainModel() }
        )
    }
}

