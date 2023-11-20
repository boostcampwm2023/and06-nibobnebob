package com.avengers.nibobnebob.data.repository

import kotlinx.coroutines.flow.Flow

interface ValidationRepository {

    fun nickValidation(
        nickName: String
    ): Flow<Unit>

    fun emailValidation(
        email: String
    ): Flow<Unit>
}