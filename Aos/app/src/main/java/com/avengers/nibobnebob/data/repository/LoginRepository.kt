package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun loginNaver() : Flow<BaseResponse<NaverLoginResponse>>
    suspend fun putData(key : String, value : String)
    suspend fun getData(key:String) : String
}