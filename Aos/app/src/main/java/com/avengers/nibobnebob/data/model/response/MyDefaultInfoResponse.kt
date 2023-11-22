package com.avengers.nibobnebob.data.model.response

data class BaseEdit(
    val data: MyDefaultInfoResponse,
    val message: String,
    val statusCode: Int
)

data class MyDefaultInfoResponse(
    val userInfo: UserEditInfo
)

data class UserEditInfo(
    val nickName: String,
    val email: String,
    val provider: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val password : Int,
)
