package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.remote.IntroAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroAPI
) : IntroRepository {

    override fun signup(body: DetailSignupRequest): Flow<Unit> = flow{
        val response = api.signup(body)
        emit(response)
    }
}