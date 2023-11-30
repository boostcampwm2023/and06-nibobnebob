package com.avengers.nibobnebob.data.model

import com.google.gson.Gson
import retrofit2.Response

enum class StatusCode{
    EMPTY,
    ERROR,
    EXCEPTION,
    ERROR_AUTH,
    ERROR_NONE
}

sealed class BaseState<out T> {
    data class Success<out T>(val data: T) : BaseState<T>()
    data class Error(val statusCode: StatusCode, val message: String) : BaseState<Nothing>()
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