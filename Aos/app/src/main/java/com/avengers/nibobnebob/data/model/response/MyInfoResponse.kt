package com.avengers.nibobnebob.data.model.response


data class MyInfoResponse(
    val userInfo: UserInfo
)

data class UserInfo(
    val nickName: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean
)

