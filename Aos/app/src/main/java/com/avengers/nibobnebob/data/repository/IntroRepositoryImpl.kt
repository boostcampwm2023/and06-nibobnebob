package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.IntroApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository {

    override fun signup(body: DetailSignupRequest): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.signup(body) }
        emit(result)
    }

    override fun loginNaver(): Flow<BaseState<NaverLoginResponse>> = flow {
        val result = runRemote { api.loginNaver() }
        emit(result)
    }
}