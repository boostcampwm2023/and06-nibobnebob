package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.data.model.response.Location
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel
import com.google.gson.annotations.SerializedName

data class UserDetailData(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val isFollow: Boolean,
    val restaurants: List<UserDetailRestaurantData>
): BaseDomainModel


data class UserDetailRestaurantData(
    val id: Int,
    val name: String,
    val location: Location,
    val address: String,
    val phoneNumber: String,
    val reviewCnt: Int,
    val category: String,
    val isMy: Boolean
): BaseDomainModel
