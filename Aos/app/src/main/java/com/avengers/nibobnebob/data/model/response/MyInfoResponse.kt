package com.avengers.nibobnebob.data.model.response

data class Base(
    val data: MyInfoResponse,
    val message: String,
    val statusCode: Int
)


data class MyInfoResponse(
    val userInfo: UserInfo
)

data class UserInfo(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean
)

