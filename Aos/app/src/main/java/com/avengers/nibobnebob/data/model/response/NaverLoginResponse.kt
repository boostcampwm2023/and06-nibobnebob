package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class NaverLoginResponse(
    @SerializedName("data")val data : String?,
    @SerializedName("message")val message : String ?,
    @SerializedName("statusCode")val code : Int ?
)
