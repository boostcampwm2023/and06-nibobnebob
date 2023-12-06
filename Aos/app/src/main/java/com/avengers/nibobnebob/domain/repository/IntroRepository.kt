package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.LoginData
import com.avengers.nibobnebob.domain.model.base.BaseState
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
        profileImage: MultipartBody.Part?
    ): Flow<BaseState<Unit>>

    fun loginNaver(
        token: String
    ): Flow<BaseState<LoginData>>

    fun loginBasic(
        email: String,
        password: String
    ): Flow<BaseState<LoginData>>
}