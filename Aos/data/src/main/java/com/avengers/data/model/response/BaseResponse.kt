package com.avengers.data.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data")
    val body: T? = null,
    val message: String,
    val statusCode: Int,
)

