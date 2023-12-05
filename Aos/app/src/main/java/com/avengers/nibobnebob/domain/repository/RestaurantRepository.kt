package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<BaseState<RestaurantDetailData>>

    fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<BaseState<Unit>>

    fun deleteRestaurant(
        restaurantId: Int,
    ): Flow<BaseState<Unit>>

    fun myRestaurantList(): Flow<BaseState<RestaurantData>>

    fun myWishList(): Flow<BaseState<WishRestaurantData>>

    fun addWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<Unit>>

    fun deleteWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<Unit>>

    fun getRestaurantIsWish(
        id: Int
    ): Flow<BaseState<RestaurantIsWishData>>
}