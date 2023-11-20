package com.avengers.nibobnebob.data.model

import com.google.gson.annotations.SerializedName

sealed class BaseResponse<out T>(
    val data: T? = null,
    val message: String? = null,
    @SerializedName("statusCode") val code : Int? = null
) {
    class Success<out T>(data: T? = null): BaseResponse<T>(data)
    class Loading<T>(data: T? = null): BaseResponse<T>(data)
    class Error<T>(message: String, data: T? = null, code : Int? = null)
        : BaseResponse<T>(data, message,code)
}