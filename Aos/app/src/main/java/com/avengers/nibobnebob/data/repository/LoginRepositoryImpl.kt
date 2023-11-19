package com.avengers.nibobnebob.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.avengers.nibobnebob.data.model.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import com.avengers.nibobnebob.data.remote.NibobNebobApi
import com.avengers.nibobnebob.presentation.util.Constants.AUTOLOGIN
import com.avengers.nibobnebob.presentation.util.Constants.TRUE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val nibobNebobApi: NibobNebobApi,
    private val dataStore: DataStore<Preferences>
) : LoginRepository {

    private val TAG = "LoginRepositoryImplDebug"

    override fun loginNaver(): Flow<BaseResponse<NaverLoginResponse>> = flow {
        try {
            val response = nibobNebobApi.postNaverLogin()
            if (response.isSuccessful) {
                putData(AUTOLOGIN, TRUE)
                response.body()?.let { result ->
                    emit(result)
                }
            } else {
                emit(BaseResponse.Error(response.message(), statusCode = response.code()))
            }
        } catch (e: Exception) {
            emit(BaseResponse.Error(e.message ?: "오류 발생"))
        }
    }


    override suspend fun putData(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit {
            it[preferencesKey] = value
        }
    }


    override suspend fun getData(key: String): String {
        val preferencesKey = stringPreferencesKey(key)
        return dataStore.data.first()[preferencesKey] ?: ""
    }
}