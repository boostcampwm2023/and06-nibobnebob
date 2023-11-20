package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.response.ValidationResponse
import kotlinx.coroutines.flow.Flow

interface ValidationRepository {

    fun nickValidation(
        nickName: String
    ): Flow<ValidationResponse>

    fun emailValidation(
        email: String
    ): Flow<ValidationResponse>
}