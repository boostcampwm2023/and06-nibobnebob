package com.avengers.nibobnebob.data.model

import com.google.gson.Gson
import retrofit2.Response


sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val statusCode: Int, val message: String) : ApiState<Nothing>()
    data class Exception(val e: Throwable) : ApiState<Nothing>()
}

suspend fun <T> runNNApi(block: suspend () -> Response<T>): ApiState<T> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiState.Success(it)
            } ?: ApiState.Error(0, "응답이 비어있습니다")
        } else {
            val errorData = Gson().fromJson(response.errorBody()?.string(), ApiState.Error::class.java)
            ApiState.Error(errorData.statusCode, errorData.message)
        }
    } catch (e: Exception) {
        ApiState.Exception(e)
    }
}