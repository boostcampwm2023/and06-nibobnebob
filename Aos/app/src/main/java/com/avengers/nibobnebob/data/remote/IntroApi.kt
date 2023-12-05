package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IntroApi {

    @POST("api/user")
    suspend fun signup(
        @Body params: DetailSignupRequest
    ): Response<Unit>

    @POST("api/auth/social-login")
    suspend fun loginNaver(
        @Header("Authorization")token : String
    ): Response<BaseResponse<LoginResponse>>

    @POST("api/auth/login")
    suspend fun loginBasic(
        @Body params: BasicLoginRequest
    ): Response<BaseResponse<LoginResponse>>
}