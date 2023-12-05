package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.ValidateData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface ValidationRepository {
    fun nickValidation(
        nickName: String
    ): Flow<BaseState<ValidateData>>

    fun emailValidation(
        email: String
    ): Flow<BaseState<ValidateData>>
}