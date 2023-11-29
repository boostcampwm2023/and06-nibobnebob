package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.FollowSearchResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApi {

    @GET("api/user/followed-list")
    suspend fun getMyFollower(): Response<BaseResponse<List<FollowListResponse>>>

    @GET("api/user/follow-list")
    suspend fun getMyFollowing(): Response<BaseResponse<List<FollowListResponse>>>

    @GET("api/user/recommended")
    suspend fun getMyRecommendFollow(): Response<BaseResponse<List<FollowListResponse>>>

    @POST("api/user/follow-list/{nickName}")
    suspend fun follow(
        @Path("nickName") nickName: String
    ): Response<BaseResponse<Unit>>

    @DELETE("api/user/follow-list/{nickName}")
    suspend fun unFollow(
        @Path("nickName") nickName: String
    ): Response<BaseResponse<Unit>>

    @GET("api/user/autocomplete/{partialUsername}")
    suspend fun searchFollow(
        @Path("partialUsername") keyword: String
    ): Response<BaseResponse<FollowSearchResponse>>
}