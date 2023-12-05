package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.IntroApi
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
        profileImage: MultipartBody.Part
    ): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.signup(email, password, provider, nickName, region, birthdate, isMale, profileImage) }
        emit(result)
    }

    override fun loginNaver(token : String): Flow<BaseState<BaseResponse<LoginResponse>>> = flow {
        val result = runRemote { api.loginNaver("$ACCESS $token") }
        emit(result)
    }

    override fun loginBasic(body: BasicLoginRequest): Flow<BaseState<BaseResponse<LoginResponse>>> = flow {
        val result = runRemote { api.loginBasic(body) }
        emit(result)
    }
}