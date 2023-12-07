package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.response.LoginResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.IntroApi
import com.avengers.nibobnebob.domain.model.LoginData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.IntroRepository
import com.avengers.nibobnebob.presentation.util.Constants.ACCESS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override fun signup(
        email: RequestBody,
        password: RequestBody,
        provider: RequestBody,
        nickName: RequestBody,
        region: RequestBody,
        birthdate: RequestBody,
        isMale: Boolean,
        profileImage: MultipartBody.Part?
    ): Flow<BaseState<LoginData>> = flow {
        profileImage?.let { image ->
            when (val result = runRemote {
                api.signup(
                    email,
                    password,
                    provider,
                    nickName,
                    region,
                    birthdate,
                    isMale,
                    image
                )
            }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                    }
                }

                is BaseState.Error -> emit(result)
            }
        } ?: run {
            when (val result = runRemote {
                api.signupNoImage(
                    email,
                    password,
                    provider,
                    nickName,
                    region,
                    birthdate,
                    isMale
                )
            }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                    }
                }

                is BaseState.Error -> emit(result)
            }
        }

    }

    override fun loginNaver(token: String): Flow<BaseState<LoginData>> = flow {
        when (val result = runRemote { api.loginNaver("$ACCESS $token") }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> emit(result)

        }
    }

    override fun loginBasic(email: String, password: String): Flow<BaseState<LoginData>> = flow {
        when (val result = runRemote { api.loginBasic(BasicLoginRequest(email, password)) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> emit(result)
        }
    }
}