package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<OldBaseState<OldBaseResponse<RestaurantDetailResponse>>>

    fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun deleteRestaurant(
        restaurantId: Int,
    ): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun myRestaurantList(): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>>

    fun myWishList(): Flow<OldBaseState<OldBaseResponse<List<WishRestaurantResponse>>>>

    fun addWishRestaurant(
        restaurantId: Int
    ): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun deleteWishRestaurant(
        restaurantId: Int
    ): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun getRestaurantIsWish(
        id: Int
    ): Flow<OldBaseState<OldBaseResponse<RestaurantIsWishResponse>>>
}