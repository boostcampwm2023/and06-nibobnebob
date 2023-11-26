package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import kotlinx.coroutines.flow.Flow

interface GlobalRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<BaseState<BaseResponse<RestaurantDetailResponse>>>
}