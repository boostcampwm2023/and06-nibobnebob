package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.MyPageEditInfoRequest
import com.avengers.nibobnebob.data.model.response.BasicResponse
import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageEditRepository {
    fun getMyPageEditInfo(): Flow<ApiState<MyPageEditInfoResponse>>

    fun putMyPageEditInfo(data : MyPageEditInfoRequest): Flow<ApiState<BasicResponse>>
}