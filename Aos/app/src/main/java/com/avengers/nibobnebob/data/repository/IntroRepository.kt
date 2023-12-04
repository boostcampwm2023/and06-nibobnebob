package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface IntroRepository {

    fun signup(
        email: String,
        password: String,
        provider: String,
        nickName: String,
        region: String,
        birthdate: String,
        isMale: Boolean,
        profileImage: MultipartBody.Part
    ): Flow<BaseState<Unit>>

    fun loginNaver(
        token: String
    ): Flow<BaseState<BaseResponse<NaverLoginResponse>>>
}