package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import com.avengers.nibobnebob.data.model.runNNApi
import com.avengers.nibobnebob.data.remote.ValidationApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(
    private val api: ValidationApi
) : ValidationRepository {

    override fun nickValidation(nickName: String): Flow<ApiState<ValidateResponse>> = flow {
        val result = runNNApi { api.nickValidation(nickName) }
        emit(result)
    }

    override fun emailValidation(email: String): Flow<ApiState<ValidateResponse>> = flow {
        val result = runNNApi { api.nickValidation(email) }
        emit(result)
    }
}