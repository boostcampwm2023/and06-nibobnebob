package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data")
    val body: T,
    val message: String,
    val statusCode: Int,
)
