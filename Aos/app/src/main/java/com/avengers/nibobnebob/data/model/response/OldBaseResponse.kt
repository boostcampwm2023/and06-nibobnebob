package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class OldBaseResponse<T>(
    @SerializedName("data")
    val body: T,
    val message: String,
    val statusCode: Int,
)
