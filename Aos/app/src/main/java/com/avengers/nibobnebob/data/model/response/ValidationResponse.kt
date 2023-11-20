package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("isexist")
    val isExist: Boolean
)
