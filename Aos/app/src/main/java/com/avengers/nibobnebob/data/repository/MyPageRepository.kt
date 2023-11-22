package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.Base
import com.avengers.nibobnebob.data.model.response.BaseEdit
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyInfo(): Flow<BaseState<Base>>

    fun getMyDefaultInfo(): Flow<BaseState<BaseEdit>>

    fun editMyInfo(data: EditMyInfoRequest): Flow<BaseState<Unit>>

}