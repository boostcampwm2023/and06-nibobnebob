package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun signup(
        body: DetailSignupRequest
    ): Flow<BaseState<Unit>>

    fun loginNaver(): Flow<BaseState<BaseResponse<NaverLoginResponse>>>
}