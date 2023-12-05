package com.avengers.nibobnebob.data.model.request

data class EditMyInfoRequest(
    val nickName: String,
    val email: String,
    val provider: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val password : String,
    val profileImage : String // TODO : 잠시보류
)
