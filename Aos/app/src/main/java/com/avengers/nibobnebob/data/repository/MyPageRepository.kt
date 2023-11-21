package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyInfo(): Flow<ApiState<MyInfoResponse>>

    fun getMyDefaultInfo(): Flow<ApiState<MyDefaultInfoResponse>>

    fun editMyInfo(data: EditMyInfoRequest): Flow<ApiState<Unit>>

}