package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
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
    ): Flow<OldBaseState<Unit>>

    fun loginNaver(
        token: String
    ): Flow<OldBaseState<OldBaseResponse<LoginResponse>>>

    fun loginBasic(
        body: BasicLoginRequest
    ): Flow<OldBaseState<OldBaseResponse<LoginResponse>>>
}