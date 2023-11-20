package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import com.avengers.nibobnebob.data.model.response.MyPageInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NnApi {
    @GET("api/user/details")
    suspend fun getMyPageInfo(
        @Header("Authorization") token : String,
    ) : Response<MyPageInfoResponse>

    @GET("api/user")
    suspend fun getMyPageEditInfo(
        @Header("Authorization") token : String,
    ) : Response<MyPageEditInfoResponse>
}