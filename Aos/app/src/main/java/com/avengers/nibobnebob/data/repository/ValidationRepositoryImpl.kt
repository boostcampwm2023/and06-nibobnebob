package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.ValidationApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(
    private val api: ValidationApi
) : ValidationRepository {

    override fun nickValidation(nickName: String): Flow<BaseState<BaseResponse<ValidateResponse>>> = flow {
        val result = runRemote { api.nickValidation(nickName) }
        emit(result)
    }

    override fun emailValidation(email: String): Flow<BaseState<BaseResponse<ValidateResponse>>> = flow {
        val result = runRemote { api.nickValidation(email) }
        emit(result)
    }
}