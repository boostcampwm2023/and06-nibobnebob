package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class NaverLoginResponse(
    @SerializedName("data") val data : NaverLoginInfo,
    @SerializedName("message") val message : String,
    @SerializedName("statusCode") val statusCode : String
)

data class NaverLoginInfo(
    @SerializedName("accessToken") val accessToken : String?,
    @SerializedName("refreshToken") val refreshToken : String?
)
