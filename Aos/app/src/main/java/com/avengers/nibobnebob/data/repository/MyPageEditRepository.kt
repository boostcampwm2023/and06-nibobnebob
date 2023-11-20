package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageEditRepository {
    fun getMyPageEditInfo(): Flow<ApiState<MyPageEditInfoResponse>>
}