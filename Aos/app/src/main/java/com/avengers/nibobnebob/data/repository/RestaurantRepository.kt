package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<BaseState<BaseResponse<RestaurantDetailResponse>>>

    fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<BaseState<BaseResponse<Unit>>>

    fun deleteRestaurant(
        restaurantId: Int,
    ): Flow<BaseState<BaseResponse<Unit>>>

    fun myRestaurantList(): Flow<BaseState<BaseResponse<List<RestaurantResponse>>>>

    fun myWishList(): Flow<BaseState<BaseResponse<List<WishRestaurantResponse>>>>

    fun addWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<BaseResponse<Unit>>>

    fun deleteWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<BaseResponse<Unit>>>

    fun getRestaurantIsWish(
        id: Int
    ): Flow<BaseState<BaseResponse<RestaurantIsWishResponse>>>
}