package com.avengers.nibobnebob.data.model.response


data class MyPageInfoResponse(
    val data: MyPageInfo,
    val message: String,
    val statusCode: Int
)


data class MyPageInfo(
    val userInfo: UserInfo
)

data class UserInfo(
    val nickName : String,
    val birthdate : String,
    val region : String,
    val isMale : Boolean
)

