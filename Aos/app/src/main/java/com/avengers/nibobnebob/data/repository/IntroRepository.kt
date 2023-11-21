package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun signup(
        body: DetailSignupRequest
    ): Flow<ApiState<Unit>>

    fun loginNaver(): Flow<ApiState<NaverLoginResponse>>
}