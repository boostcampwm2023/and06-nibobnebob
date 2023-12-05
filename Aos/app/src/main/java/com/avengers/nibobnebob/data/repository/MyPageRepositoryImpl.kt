package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.data.model.oldRunRemote
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

    override fun getMyInfo(): Flow<OldBaseState<OldBaseResponse<MyInfoResponse>>> = flow {
        val result = oldRunRemote { api.getMyInfo() }
        emit(result)
    }

    override fun getMyDefaultInfo(): Flow<OldBaseState<OldBaseResponse<MyDefaultInfoResponse>>> = flow {
        val result = oldRunRemote { api.getMyDefaultInfo() }
        emit(result)
    }

    override fun editMyInfo(data: EditMyInfoRequest): Flow<OldBaseState<Unit>> = flow {
        val result = oldRunRemote { api.editMyInfo(data) }
        emit(result)
    }

    override fun logout(): Flow<OldBaseState<Unit>> = flow {
        val result = oldRunRemote { api.logout() }
        dataStoreManager.deleteAccessToken()
        dataStoreManager.deleteRefreshToken()
        emit(result)

    }

    override fun withdraw(): Flow<OldBaseState<Unit>> = flow {
        when (val result = oldRunRemote { api.withdraw() }) {
            is OldBaseState.Success -> {
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