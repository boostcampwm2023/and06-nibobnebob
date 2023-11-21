package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.EditMyInfoRequest
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface MyPageApi {
    @GET("api/user/details")
    suspend fun getMyInfo(
        @Header("Authorization") token : String,
    ) : Response<MyInfoResponse>

    @GET("api/user")
    suspend fun getMyDefaultInfo(
        @Header("Authorization") token : String,
    ) : Response<MyDefaultInfoResponse>

    @PUT("api/user")
    suspend fun editMyInfo(
        @Header("Authorization") token : String,
        @Body data : EditMyInfoRequest
    ) : Response<Unit>

}