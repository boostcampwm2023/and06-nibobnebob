package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import kotlinx.coroutines.flow.Flow

interface ValidationRepository {

    fun nickValidation(
        nickName: String
    ): Flow<BaseState<BaseResponse<ValidateResponse>>>

    fun emailValidation(
        email: String
    ): Flow<BaseState<BaseResponse<ValidateResponse>>>
}