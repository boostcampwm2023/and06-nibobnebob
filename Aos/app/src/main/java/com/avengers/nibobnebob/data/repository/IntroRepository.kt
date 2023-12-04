package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun signup(
        body: DetailSignupRequest
    ): Flow<BaseState<Unit>>

    fun loginNaver(
        token: String
    ): Flow<BaseState<BaseResponse<NaverLoginResponse>>>

    fun loginBasic(
        body: BasicLoginRequest
    ): Flow<BaseState<Unit>>
}