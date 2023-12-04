package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class MyDefaultInfoResponse(
    @SerializedName("userInfo") val userInfo: UserEditInfo
)

data class UserEditInfo(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("email") val email: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("birthdate") val birthdate: String,
    @SerializedName("region") val region: String,
    @SerializedName("isMale") val isMale: Boolean,
    @SerializedName("password") val password: Int,
    @SerializedName("profileImage") val profileImage: String
)
