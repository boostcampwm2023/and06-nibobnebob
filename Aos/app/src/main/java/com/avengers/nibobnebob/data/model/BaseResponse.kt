package com.avengers.nibobnebob.data.model

sealed class BaseResponse<out T>(
    val data: T? = null,
    val message: String? = null,
    val statusCode : Int? = null
) {
    class Success<out T>(data: T? = null): BaseResponse<T>(data)
    class Loading<T>(data: T? = null): BaseResponse<T>(data)
    class Error<T>(message: String, data: T? = null,statusCode : Int? = null)
        : BaseResponse<T>(data, message,statusCode)
}