package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<BaseState<BaseResponse<RestaurantDetailResponse>>>

    fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<BaseState<BaseResponse<Unit>>>
}