package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.oldRunRemote
import com.avengers.nibobnebob.data.model.response.FollowingResponse
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
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
    ): Flow<OldBaseState<OldBaseResponse<List<SearchRestaurantResponse>>>> = flow {
        val result = oldRunRemote { api.searchRestaurant(name, radius, longitude, latitude) }
        emit(result)
    }

    override fun followList(): Flow<OldBaseState<OldBaseResponse<List<FollowingResponse>>>> = flow {
        val result = oldRunRemote { api.followList() }
        emit(result)
    }


    override fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>> = flow {
        val result = oldRunRemote { api.filterRestaurantList(filter, location, radius) }
        emit(result)
    }

    override fun nearRestaurantList(
        radius: String,
        longitude: String,
        latitude: String
    ): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>> = flow {
        val result = oldRunRemote { api.nearRestaurantList(radius, longitude, latitude) }
        emit(result)
    }
}