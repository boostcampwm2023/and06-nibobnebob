package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import com.avengers.nibobnebob.data.remote.NibobNebobApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val nibobNebobApi: NibobNebobApi,
    private val dataStoreManager: DataStoreManager
) : LoginRepository {

    private val TAG = "LoginRepositoryImplDebug"

    override fun loginNaver(): Flow<ApiState<NaverLoginResponse>> = flow {
        try {
            val response = nibobNebobApi.postNaverLogin()
            if (response.isSuccessful) {
                response.body()?.let{
                    dataStoreManager.putAutoLogin(true)
                    dataStoreManager.putAccessToken(it.data.accessToken.toString())
                    dataStoreManager.putRefreshToken(it.data.refreshToken.toString())
                }
                emit(ApiState.Success(response.body()!!))
            } else {
                emit(ApiState.Error(response.code(), response.message()))
            }
        } catch (e: Exception) {
            emit(ApiState.Exception(e))
        }
    }
}