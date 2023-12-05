package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.UserDetailResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FollowApi {

    @GET("api/user/followed-list")
    suspend fun getMyFollower(): Response<OldBaseResponse<List<FollowListResponse>>>

    @GET("api/user/follow-list")
    suspend fun getMyFollowing(): Response<OldBaseResponse<List<FollowListResponse>>>

    @GET("api/user/recommended")
    suspend fun getMyRecommendFollow(): Response<OldBaseResponse<List<FollowListResponse>>>

    @POST("api/user/follow-list/{nickName}")
    suspend fun follow(
        @Path("nickName") nickName: String
    ): Response<OldBaseResponse<Unit>>

    @DELETE("api/user/follow-list/{nickName}")
    suspend fun unFollow(
        @Path("nickName") nickName: String
    ): Response<OldBaseResponse<Unit>>

    @GET("api/user/autocomplete/{partialUsername}")
    suspend fun searchFollow(
        @Path("partialUsername") keyword: String,
        @Query("region") region: List<String>
    ): Response<OldBaseResponse<List<FollowListResponse>>>

    @GET("api/user/{nickName}/details")
    suspend fun getUserDetail(
        @Path("nickName") nick: String
    ): Response<OldBaseResponse<UserDetailResponse>>
}