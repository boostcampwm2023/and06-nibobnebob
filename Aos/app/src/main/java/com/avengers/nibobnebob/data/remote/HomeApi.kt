package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FilterRestaurantResponse
import com.avengers.nibobnebob.data.model.response.FollowingResponse
import com.avengers.nibobnebob.data.model.response.MyRestaurantResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET("api/restaurant/autocomplete/{partialRestaurantName}")
    suspend fun searchRestaurant(
        @Path("partialRestaurantName") name: String,
        @Query("location") location: String,
        @Query("radius") radius: String
    ): Response<BaseResponse<List<SearchRestaurantResponse>>>

    @GET("api/user/follow-list")
    suspend fun followList() : Response<BaseResponse<List<FollowingResponse>>>

    //내 맛집 리스트
    @GET("api/user/restaurant")
    suspend fun myRestaurantList() : Response<BaseResponse<List<MyRestaurantResponse>>>

    //filter 맛집 리스트
    @GET("api/restaurant")
    suspend fun filterRestaurantList(
        @Query("filter") filter : String,
        @Query("location") location : String,
        @Query("radius") radius : Int
    ): Response<BaseResponse<List<FilterRestaurantResponse>>>
}