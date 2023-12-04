package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.IntroApi
import com.avengers.nibobnebob.presentation.util.Constants.ACCESS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override fun signup(
        email: String,
        password: String,
        provider: String,
        nickName: String,
        region: String,
        birthdate: String,
        isMale: Boolean,
        profileImage: MultipartBody.Part
    ): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.signup(email, password, provider, nickName, region, birthdate, isMale, profileImage) }
        emit(result)
    }

    override fun loginNaver(token : String): Flow<BaseState<BaseResponse<NaverLoginResponse>>> = flow {
        val result = runRemote { api.loginNaver("$ACCESS $token") }
        emit(result)
    }
}