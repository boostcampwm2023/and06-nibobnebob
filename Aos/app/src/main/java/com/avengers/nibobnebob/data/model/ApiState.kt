package com.avengers.nibobnebob.data.model


sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val statusCode: Int, val message: String) : ApiState<Nothing>()
    data class Exception(val e: Throwable) : ApiState<Nothing>()
}