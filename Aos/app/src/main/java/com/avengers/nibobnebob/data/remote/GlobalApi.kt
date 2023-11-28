package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GlobalApi {

    @GET("api/restaurant/{restaurantId}/details")
    suspend fun restaurantDetail(
        @Path("restaurantId") restaurantId : Int
    ) : Response<BaseResponse<RestaurantDetailResponse>>

    @POST("api/user/restaurant/{restaurantId}")
    suspend fun addRestaurant(
        @Path("restaurantId") restaurantId: Int,
        @Body params: AddRestaurantRequest
    ) : Response<BaseResponse<Unit>>
}