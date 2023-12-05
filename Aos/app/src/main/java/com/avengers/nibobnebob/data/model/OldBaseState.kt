package com.avengers.nibobnebob.data.model

import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.google.gson.Gson
import retrofit2.Response

enum class StatusDataCode{
    EMPTY,
    ERROR,
    EXCEPTION,
    ERROR_AUTH,
    ERROR_NONE
}

sealed class OldBaseState<out T> {
    data class Success<out T>(val data: T) : OldBaseState<T>()
    data class Error(val statusDataCode: StatusDataCode, val message: String) : OldBaseState<Nothing>()
}

suspend fun <T> oldRunRemote(block: suspend () -> Response<T>): OldBaseState<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                OldBaseState.Success(it)
            } ?: OldBaseState.Error(StatusDataCode.EMPTY, "응답이 비어있습니다")
        } else {
            val errorData = Gson().fromJson(response.errorBody()?.string(), OldBaseState.Error::class.java)
            when(response.code()){
                401 -> OldBaseState.Error(StatusDataCode.ERROR_AUTH, errorData.message)
                404 -> OldBaseState.Error(StatusDataCode.ERROR_NONE, errorData.message)
                else -> OldBaseState.Error(StatusDataCode.ERROR, errorData.message)
            }
        }
    } catch (e: Exception) {
        OldBaseState.Error(StatusDataCode.EXCEPTION, e.message.toString())
    }
}


suspend fun <T> runRemote(block: suspend () -> Response<T>): BaseState<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                BaseState.Success(it)
            } ?: BaseState.Error(StatusCode.EMPTY, "응답이 비어있습니다")
        } else {
            val errorData = Gson().fromJson(response.errorBody()?.string(), BaseState.Error::class.java)
            when(response.code()){
                401 -> BaseState.Error(StatusCode.ERROR_AUTH, errorData.message)
                404 -> BaseState.Error(StatusCode.ERROR_NONE, errorData.message)
                else -> BaseState.Error(StatusCode.ERROR, errorData.message)
            }
        }
    } catch (e: Exception) {
        BaseState.Error(StatusCode.EXCEPTION, e.message.toString())
    }
}



