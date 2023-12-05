package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.NaverLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IntroApi {

    @Multipart
    @POST("api/user")
    suspend fun signup(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("provider") provider: RequestBody,
        @Part("nickName") nickName: RequestBody,
        @Part("region") region : RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("isMale") isMale: Boolean,
        @Part profileImage : MultipartBody.Part
    ): Response<Unit>

    @POST("api/auth/social-login")
    suspend fun loginNaver(
        @Header("Authorization")token : String
    ): Response<BaseResponse<NaverLoginResponse>>
}