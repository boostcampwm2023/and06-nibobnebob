package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.BasicLoginRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
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
        @Part("region") region: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("isMale") isMale: Boolean,
        @Part profileImage: MultipartBody.Part
    ): Response<BaseResponse<LoginResponse>>

    @Multipart
    @POST("api/user")
    suspend fun signupNoImage(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("provider") provider: RequestBody,
        @Part("nickName") nickName: RequestBody,
        @Part("region") region: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("isMale") isMale: Boolean,
    ): Response<BaseResponse<LoginResponse>>

    @POST("api/auth/social-login")
    suspend fun loginNaver(
        @Header("Authorization") token: String
    ): Response<BaseResponse<LoginResponse>>

    @POST("api/auth/login")
    suspend fun loginBasic(
        @Body params: BasicLoginRequest
    ): Response<BaseResponse<LoginResponse>>
}