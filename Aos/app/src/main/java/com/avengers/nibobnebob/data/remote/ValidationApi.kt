package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.ValidateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ValidationApi {

    @GET("api/user/nickname/{nickname}/exists")
    suspend fun nickValidation(
        @Path("nickname") nickname: String
    ): Response<BaseResponse<ValidateResponse>>

    @GET("api/user/email/{email}/exists")
    suspend fun emailValidation(
        @Path("email") email: String
    ): Response<BaseResponse<ValidateResponse>>
}