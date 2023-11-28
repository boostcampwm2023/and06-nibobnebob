package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FilterRestaurantResponse
import com.avengers.nibobnebob.data.model.response.FollowingResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.HomeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val api: HomeApi) : HomeRepository {
    override fun searchRestaurant(
        name: String,
        radius: String?,
        longitude: String?,
        latitude: String?
    ): Flow<BaseState<BaseResponse<List<SearchRestaurantResponse>>>> = flow {
        val result = runRemote { api.searchRestaurant(name, radius, longitude, latitude) }
        emit(result)
    }

    override fun followList(): Flow<BaseState<BaseResponse<List<FollowingResponse>>>> = flow {
        val result = runRemote { api.followList() }
        emit(result)
    }


    override fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<BaseState<BaseResponse<List<FilterRestaurantResponse>>>> = flow {
        val result = runRemote { api.filterRestaurantList(filter, location, radius) }
        emit(result)
    }
}