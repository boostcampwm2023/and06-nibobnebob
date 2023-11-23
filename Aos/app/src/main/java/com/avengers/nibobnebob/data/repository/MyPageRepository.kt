package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyInfo(): Flow<BaseState<BaseResponse<MyInfoResponse>>>

    fun getMyDefaultInfo(): Flow<BaseState<BaseResponse<MyDefaultInfoResponse>>>

    fun editMyInfo(data: EditMyInfoRequest): Flow<BaseState<Unit>>

    suspend fun logout()

    fun withdraw(): Flow<BaseState<Unit>>

}