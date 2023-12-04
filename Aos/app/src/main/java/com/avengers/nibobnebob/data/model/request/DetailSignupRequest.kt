package com.avengers.nibobnebob.data.model.request

import okhttp3.MultipartBody

data class DetailSignupRequest(
    val email: String,
    val password: String? = "",
    val provider: String,
    val nickName: String,
    val region: String,
    val birthdate: String,
    val isMale: Boolean,
    val profileImage: MultipartBody.Part
)
