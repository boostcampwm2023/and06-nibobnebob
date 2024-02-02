package com.avengers.data.remote

import com.avengers.data.model.response.ValidateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ValidationApi {

    @GET("api/user/nickname/{nickname}/exists")
    suspend fun nickValidation(
        @Path("nickname") nickname: String
    ): Response<com.avengers.data.model.response.BaseResponse<ValidateResponse>>

    @GET("api/user/email/{email}/exists")
    suspend fun emailValidation(
        @Path("email") email: String
    ): Response<com.avengers.data.model.response.BaseResponse<ValidateResponse>>
}