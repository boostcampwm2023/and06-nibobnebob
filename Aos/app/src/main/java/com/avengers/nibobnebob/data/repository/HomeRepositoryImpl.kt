package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.HomeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val api : HomeApi) : HomeRepository{
    override fun searchRestaurant(
        name: String,
        location: String,
        radius: String
    ): Flow<BaseState<BaseResponse<List<SearchRestaurantResponse>>>> = flow  {
        val result = runRemote { api.searchRestaurant(name, location, radius) }
        emit(result)
    }

}