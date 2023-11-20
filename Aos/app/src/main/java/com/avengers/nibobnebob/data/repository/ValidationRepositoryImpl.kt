package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.remote.ValidationApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidationRepositoryImpl @Inject constructor(
    private val api: ValidationApi
): ValidationRepository {

    override fun nickValidation(nickName: String): Flow<Unit> = flow{
        val response = api.nickValidation(nickName)
        emit(response)
    }

    override fun emailValidation(email: String): Flow<Unit> = flow{
        val response = api.emailValidation(email)
        emit(response)
    }
}