package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun searchRestaurant(
        name: String,
        radius: String?,
        longitude: String?,
        latitude: String?
    ): Flow<BaseState<BaseResponse<List<SearchRestaurantResponse>>>>

}