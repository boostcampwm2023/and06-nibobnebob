package com.avengers.nibobnebob.data.repository_impl

import android.util.Log
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.Base
import com.avengers.nibobnebob.data.model.response.BaseEdit
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.avengers.nibobnebob.data.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val api: MyPageApi) : MyPageRepository {
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWQiOjIsImlhdCI6MjAxNjIzOTAyMn0.VMvtPcawKVzNyOV08lrArxUvM-XxowWIknsPFkjRTws"
    override fun getMyInfo(): Flow<BaseState<Base>> = flow {
        api.getMyInfo(token).apply { Log.d("TEST", "///${body()}") }
        val result = runRemote { api.getMyInfo(token) }
        emit(result)

    }

    override fun getMyDefaultInfo(): Flow<BaseState<BaseEdit>> = flow {
        val result = runRemote { api.getMyDefaultInfo(token) }
        emit(result)
    }

    override fun editMyInfo(data: EditMyInfoRequest): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.editMyInfo(token, data) }
        emit(result)
    }

}