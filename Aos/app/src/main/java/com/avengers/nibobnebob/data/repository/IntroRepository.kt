package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IntroRepository {

    fun signup(
        email: RequestBody,
        password: RequestBody,
        provider: RequestBody,
        nickName: RequestBody,
        region: RequestBody,
        birthdate: RequestBody,
        isMale: Boolean,
        profileImage: MultipartBody.Part
    ): Flow<BaseState<Unit>>

    fun loginNaver(
        token: String
    ): Flow<BaseState<BaseResponse<LoginResponse>>>

    fun loginBasic(
        body: BasicLoginRequest
    ): Flow<BaseState<BaseResponse<LoginResponse>>>
}