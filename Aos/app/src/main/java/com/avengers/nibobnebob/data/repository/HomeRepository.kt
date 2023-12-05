package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.response.FollowingResponse
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun searchRestaurant(
        name: String,
        radius: String?,
        longitude: String?,
        latitude: String?
    ): Flow<OldBaseState<OldBaseResponse<List<SearchRestaurantResponse>>>>

    fun followList(): Flow<OldBaseState<OldBaseResponse<List<FollowingResponse>>>>

    fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>>

    fun nearRestaurantList(
        radius: String,
        longitude: String,
        latitude: String,
    ): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>>
}