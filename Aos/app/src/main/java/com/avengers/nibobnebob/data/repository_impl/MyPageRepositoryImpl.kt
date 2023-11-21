package com.avengers.nibobnebob.data.repository_impl

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.data.model.runNNApi
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.avengers.nibobnebob.data.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val api: MyPageApi) : MyPageRepository {
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWQiOjIsImlhdCI6MjAxNjIzOTAyMn0.VMvtPcawKVzNyOV08lrArxUvM-XxowWIknsPFkjRTws"
    override fun getMyInfo(): Flow<ApiState<MyInfoResponse>> = flow {
        val result = runNNApi { api.getMyInfo(token) }
        emit(result)

    }

    override fun getMyDefaultInfo(): Flow<ApiState<MyDefaultInfoResponse>> = flow {
        val result = runNNApi { api.getMyDefaultInfo(token) }
        emit(result)
    }

    override fun editMyInfo(data: EditMyInfoRequest): Flow<ApiState<Unit>> = flow {
        val result = runNNApi { api.editMyInfo(token, data) }
        emit(result)
    }

}