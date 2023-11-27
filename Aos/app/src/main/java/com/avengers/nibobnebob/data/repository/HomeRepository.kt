package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FilterRestaurantResponse
import com.avengers.nibobnebob.data.model.response.MyRestaurantResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun searchRestaurant(
        name: String,
        location: String,
        radius: String
    ): Flow<BaseState<BaseResponse<List<SearchRestaurantResponse>>>>

    fun followList(): Flow<BaseState<BaseResponse<List<String>>>>

    fun myRestaurantList() : Flow<BaseState<BaseResponse<List<MyRestaurantResponse>>>>

    fun filterRestaurantList(
        filter : String,
        location: String,
        radius: Int
    ) : Flow<BaseState<BaseResponse<List<FilterRestaurantResponse>>>>

}