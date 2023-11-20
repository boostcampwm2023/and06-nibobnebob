package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun loginNaver(): Flow<NaverLoginResponse>
}