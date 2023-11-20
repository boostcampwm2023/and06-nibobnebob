package com.avengers.nibobnebob.data.repository

import android.util.Log
import com.avengers.nibobnebob.app.DataStoreManager
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

    override fun loginNaver(): Flow<NaverLoginResponse> = flow {
        try {
            val response = nibobNebobApi.postNaverLogin()
            if (response.isSuccessful) {
                dataStoreManager.putAutoLogin(true)
                response.body()?.let { result ->
                    dataStoreManager.putAccessToken(result.data.toString())
                    emit(result)
                    //TODO : 추후 refresh,access token 저장
                }
            } else {
                emit(response.body() as NaverLoginResponse)
            }
        } catch (e: Exception) {
            Log.d(TAG, "EXCEPTION 발생 ${e.message}")
        }
    }


}