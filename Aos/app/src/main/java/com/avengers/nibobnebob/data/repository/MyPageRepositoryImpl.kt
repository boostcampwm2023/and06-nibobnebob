package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.MyInfoResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.domain.model.MyInfoData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.MyPageRepository
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val api: MyPageApi,
    private val dataStoreManager: DataStoreManager
) : MyPageRepository {

    override fun getMyInfo(): Flow<BaseState<MyInfoData>> = flow {
        when (val result = runRemote { api.getMyInfo() }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getMyDefaultInfo(): Flow<BaseState<MyDefaultInfoData>> = flow {
        when (val result = runRemote { api.getMyDefaultInfo() }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun editMyInfo(
        nickName: String,
        email: String,
        provider: String,
        birthdate: String,
        region: String,
        isMale: Boolean,
        isImageChanged: Boolean,
        profileImage: MultipartBody.Part?
    ): Flow<BaseState<Unit>> = flow {
        val result = runRemote {
            api.editInfo(
                email = email.toRequestBody(),
                provider = provider.toRequestBody(),
                nickName = nickName.toRequestBody(),
                region = region.toRequestBody(),
                birthdate = birthdate.toRequestBody(),
                isMale = isMale,
                isImageChanged = isImageChanged,
                profileImage = if (isImageChanged) profileImage else null,
                profileImageString = if (isImageChanged) null else "".toRequestBody()
            )
        }
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