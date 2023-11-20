package com.avengers.nibobnebob.data.repository_impl

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import com.avengers.nibobnebob.data.remote.NnApi
import com.avengers.nibobnebob.data.repository.MyPageEditRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageEditRepositoryImpl @Inject constructor(private val nnApi: NnApi) :
    MyPageEditRepository {
    private val token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWQiOjIsImlhdCI6MjAxNjIzOTAyMn0.VMvtPcawKVzNyOV08lrArxUvM-XxowWIknsPFkjRTws"

    override fun getMyPageEditInfo(): Flow<ApiState<MyPageEditInfoResponse>> = flow {
        // 여기는 함수 만들어서 빼면 될듯
        try {
            val response = nnApi.getMyPageEditInfo(token)
            if (response.isSuccessful) {
                emit(ApiState.Success(response.body()!!))
            } else {
                emit(ApiState.Error(response.code(), response.message()))
            }
        } catch (e: Exception) {
            emit(ApiState.Exception(e))
        }
    }
}