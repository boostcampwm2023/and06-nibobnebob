package com.avengers.data.remote

import com.avengers.data.model.request.RefreshTokenRequest
import com.avengers.data.model.response.BaseResponse
import com.avengers.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {

    @POST("api/auth/refresh-token")
    suspend fun refreshAccessToken(@Body refreshToken: com.avengers.data.model.request.RefreshTokenRequest) : Response<com.avengers.data.model.response.BaseResponse<com.avengers.data.model.response.LoginResponse>>
}