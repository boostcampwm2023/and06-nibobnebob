package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.MyPageInfoResponse
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyPageInfo(): Flow<ApiState<MyPageInfoResponse>>
}