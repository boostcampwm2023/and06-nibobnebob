package com.avengers.nibobnebob.data.model.response

data class BaseResponse<T>(
    val data: T,
    val message: String,
    val statusCode: Int,
)
