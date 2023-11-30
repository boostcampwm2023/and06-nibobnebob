package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName


data class NaverLoginResponse(
    @SerializedName("accessToken") val accessToken : String?,
    @SerializedName("refreshToken") val refreshToken : String?
)
