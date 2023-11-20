package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.remote.IntroAPI
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroAPI
) : IntroRepository {

    override fun signup(body: DetailSignupRequest): Flow<ApiState<Unit>> = flow {
        runCatching { api.signup(body) }
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