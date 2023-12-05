package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyInfo(): Flow<OldBaseState<OldBaseResponse<MyInfoResponse>>>

    fun getMyDefaultInfo(): Flow<OldBaseState<OldBaseResponse<MyDefaultInfoResponse>>>

    fun editMyInfo(data: EditMyInfoRequest): Flow<OldBaseState<Unit>>

    fun logout(): Flow<OldBaseState<Unit>>

    fun withdraw(): Flow<OldBaseState<Unit>>

}