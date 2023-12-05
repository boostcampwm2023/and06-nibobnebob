package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApi {

    @GET("api/restaurant/{restaurantId}/details")
    suspend fun restaurantDetail(
        @Path("restaurantId") restaurantId: Int
    ): Response<OldBaseResponse<RestaurantDetailResponse>>

    @POST("api/user/restaurant/{restaurantId}")
    suspend fun addRestaurant(
        @Path("restaurantId") restaurantId: Int,
        @Body params: AddRestaurantRequest
    ): Response<OldBaseResponse<Unit>>

    @DELETE("api/user/restaurant/{restaurantid}")
    suspend fun deleteRestaurant(
        @Path("restaurantid") restaurantId: Int
    ): Response<OldBaseResponse<Unit>>

    // 내 맛집 리스트
    @GET("api/user/restaurant")
    suspend fun myRestaurantList(): Response<OldBaseResponse<List<RestaurantResponse>>>

    // 내 위시 리스트
    @GET("api/user/wish-restaurant")
    suspend fun myWishList(): Response<OldBaseResponse<List<WishRestaurantResponse>>>

    @POST("api/user/wish-restaurant/{restaurantId}")
    suspend fun addWishRestaurant(
        @Path("restaurantId") restaurantId: Int
    ): Response<OldBaseResponse<Unit>>

    @DELETE("api/user/wish-restaurant/{restaurantId}")
    suspend fun deleteWishRestaurant(
        @Path("restaurantId") restaurantId: Int
    ): Response<OldBaseResponse<Unit>>

    @GET("api/user/state/wish-restaurant")
    suspend fun getRestaurantIsWish(
        @Query("restaurantid") id: Int
    ): Response<OldBaseResponse<RestaurantIsWishResponse>>

}