package com.avengers.nibobnebob.data.model.request


data class EditMyInfoNoImageRequest(
    val nickName: String,
    val email: String,
    val provider: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean
)