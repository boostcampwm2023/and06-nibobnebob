package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val api: MyPageApi,
    private val dataStoreManager: DataStoreManager
) : MyPageRepository {

    override fun getMyInfo(): Flow<BaseState<BaseResponse<MyInfoResponse>>> = flow {
        val result = runRemote { api.getMyInfo() }
        emit(result)
    }

    override fun getMyDefaultInfo(): Flow<BaseState<BaseResponse<MyDefaultInfoResponse>>> = flow {
        val result = runRemote { api.getMyDefaultInfo() }
        emit(result)
    }

    override fun editMyInfo(data: EditMyInfoRequest): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.editMyInfo(data) }
        emit(result)
    }

    override fun logout(): Flow<BaseState<Unit>> = flow {
        val result = runRemote { api.logout() }
        dataStoreManager.deleteAccessToken()
        dataStoreManager.deleteRefreshToken()
        emit(result)

    }

    override fun withdraw(): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.withdraw() }) {
            is BaseState.Success -> {
                dataStoreManager.deleteAccessToken()
                dataStoreManager.deleteRefreshToken()

                NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                    override fun onError(errorCode: Int, message: String) {}

                    override fun onFailure(httpStatus: Int, message: String) {}

                    override fun onSuccess() {}
                })

                emit(result)
            }

            else -> Unit
        }
    }

}