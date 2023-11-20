package com.avengers.nibobnebob.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckSameNickResponse(
    val data: CheckSameNick,
    val message: String,
    val statusCode: Int
)

data class CheckSameNick(
    @SerializedName("isexist") val isExist : Boolean
)
