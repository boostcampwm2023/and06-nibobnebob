package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.ValidationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ValidationApi {

    @GET("/api/user/nickname/{nickname}/exists")
    suspend fun nickValidation(
        @Path("nickname") nickname: String
    ): ValidationResponse

    @GET("/api/user/email/{email}/exists")
    suspend fun emailValidation(
        @Path("email") email: String
    ): ValidationResponse
}