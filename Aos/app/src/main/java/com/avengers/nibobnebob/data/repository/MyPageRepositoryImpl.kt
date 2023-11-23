package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.avengers.nibobnebob.data.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val api: MyPageApi) : MyPageRepository {

    override fun getMyInfo(): Flow<BaseState<BaseResponse<MyInfoResponse>>> = flow {
        val result = runRemote { api.getMyInfo() }
        emit(result)

    }

    override fun getMyDefaultInfo(): Flow<BaseState<BaseResponse<MyDefaultInfoResponse>>> = flow {
        val result = runRemote { api.getMyDefaultInfo() }
        emit(result)
    }

    override fun editMyInfo(data: EditMyInfoRequest): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.editMyInfo( data) }
        emit(result)
    }

    override fun logout(): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.logout() }
        emit(result)
    }

}