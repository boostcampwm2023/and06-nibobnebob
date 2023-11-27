package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET("api/restaurant/autocomplete/{partialRestaurantName}")
    suspend fun searchRestaurant(
        @Path("partialRestaurantName") name: String,
        @Query("radius") radius: String?,
        @Query("longitude") longitude: String?,
        @Query("latitude") latitude: String?
    ): Response<BaseResponse<List<SearchRestaurantResponse>>>
}