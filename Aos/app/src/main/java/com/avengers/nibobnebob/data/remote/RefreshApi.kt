package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.RefreshTokenRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {

    @POST("api/auth/refresh-token")
    suspend fun refreshAccessToken(@Body refreshToken: RefreshTokenRequest) : Response<BaseResponse<LoginResponse>>
}