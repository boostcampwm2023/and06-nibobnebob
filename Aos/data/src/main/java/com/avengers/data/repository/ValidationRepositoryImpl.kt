package com.avengers.data.repository

import com.avengers.data.model.response.ValidateResponse.Companion.toDomainModel
import com.avengers.data.model.runRemote
import com.avengers.data.remote.ValidationApi
import com.avengers.nibobnebob.domain.model.ValidateData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.ValidationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(
    private val api: ValidationApi
) : ValidationRepository {

    override fun nickValidation(nickName: String): Flow<BaseState<ValidateData>> = flow {
        when (val result = runRemote { api.nickValidation(nickName) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun emailValidation(email: String): Flow<BaseState<ValidateData>> = flow {
        when (val result = runRemote { api.emailValidation(email) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }
}