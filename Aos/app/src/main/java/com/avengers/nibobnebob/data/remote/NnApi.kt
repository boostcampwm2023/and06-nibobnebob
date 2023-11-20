package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.MyPageEditInfoRequest
import com.avengers.nibobnebob.data.model.response.BasicResponse
import com.avengers.nibobnebob.data.model.response.CheckSameNickResponse
import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import com.avengers.nibobnebob.data.model.response.MyPageInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface NnApi {
    @GET("api/user/details")
    suspend fun getMyPageInfo(
        @Header("Authorization") token : String,
    ) : Response<MyPageInfoResponse>

    @GET("api/user")
    suspend fun getMyPageEditInfo(
        @Header("Authorization") token : String,
    ) : Response<MyPageEditInfoResponse>

    @PUT("api/user")
    suspend fun putMyPageEditInfo(
        @Header("Authorization") token : String,
        @Body data : MyPageEditInfoRequest
    ) : Response<BasicResponse>

    @GET("api/user/nickname/{nickname}/exists")
    suspend fun getCheckSameNickname(
        @Path("nickname") nick : String
    ) : Response<CheckSameNickResponse>
}