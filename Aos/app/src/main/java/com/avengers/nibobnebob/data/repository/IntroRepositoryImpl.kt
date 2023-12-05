package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import com.avengers.nibobnebob.data.model.oldRunRemote
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
    ): Flow<OldBaseState<Unit>> = flow {
        val result = oldRunRemote { api.signup(email, password, provider, nickName, region, birthdate, isMale, profileImage) }
        emit(result)
    }

    override fun loginNaver(token : String): Flow<OldBaseState<OldBaseResponse<LoginResponse>>> = flow {
        val result = oldRunRemote { api.loginNaver("$ACCESS $token") }
        emit(result)
    }

    override fun loginBasic(body: BasicLoginRequest): Flow<OldBaseState<OldBaseResponse<LoginResponse>>> = flow {
        val result = oldRunRemote { api.loginBasic(body) }
        emit(result)
    }
}