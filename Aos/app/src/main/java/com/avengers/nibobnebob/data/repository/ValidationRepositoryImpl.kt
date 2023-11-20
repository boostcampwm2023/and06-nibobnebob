package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import com.avengers.nibobnebob.data.remote.ValidationApi
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(
    private val api: ValidationApi
) : ValidationRepository {

    override fun nickValidation(nickName: String): Flow<ApiState<ValidateResponse>> = flow {
        runCatching { api.nickValidation(nickName) }
            .onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(ApiState.Success(it))
                    } ?: run {
                        emit(ApiState.Error(0, "응답이 비어있습니다"))
                    }
                } else {
                    val errorData =
                        Gson().fromJson(response.errorBody()?.string(), ApiState.Error::class.java)
                    emit(ApiState.Error(errorData.statusCode, errorData.message))
                }
            }.onFailure {
                emit(ApiState.Exception(it))
            }
    }

    override fun emailValidation(email: String): Flow<ApiState<ValidateResponse>> = flow {
        runCatching { api.emailValidation(email) }
            .onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(ApiState.Success(it))
                    } ?: run {
                        emit(ApiState.Error(0, "응답이 비어있습니다"))
                    }
                } else {
                    val errorData =
                        Gson().fromJson(response.errorBody()?.string(), ApiState.Error::class.java)
                    emit(ApiState.Error(errorData.statusCode, errorData.message))
                }
            }.onFailure {
                emit(ApiState.Exception(it))
            }
    }
}