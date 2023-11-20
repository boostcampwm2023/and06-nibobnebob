package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class ValidationResponse(
    val data: ValidateState,
    val message: String,
    val statusCode: Int
)

data class ValidateState(
    @SerializedName("isexist")
    val isExist: Boolean
)
