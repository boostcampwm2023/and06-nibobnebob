package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import kotlinx.coroutines.flow.Flow

interface ValidationRepository {

    fun nickValidation(
        nickName: String
    ): Flow<ApiState<ValidateResponse>>

    fun emailValidation(
        email: String
    ): Flow<ApiState<ValidateResponse>>
}