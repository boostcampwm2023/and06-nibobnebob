package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import retrofit2.Response
import retrofit2.http.POST

interface NibobNebobApi {

    @POST("api/auth/social-login")
    suspend fun postNaverLogin(): Response<NaverLoginResponse>
}