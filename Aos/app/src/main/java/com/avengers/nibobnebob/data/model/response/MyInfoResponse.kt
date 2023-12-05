package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.google.gson.annotations.SerializedName


data class MyInfoResponse(
    @SerializedName("userInfo") val userInfo: UserInfo
)

data class UserInfo(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("birthdate") val birthdate: String,
    @SerializedName("region") val region: String,
    @SerializedName("isMale") val isMale: Boolean,
    @SerializedName("profileImage") val profileImage: String
)

