package com.avengers.nibobnebob.data.repository

import android.util.Log
import com.avengers.nibobnebob.data.model.response.RestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.remote.HomeApi
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val api : HomeApi
) : HomeRepository {
    override fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<BaseState<List<RestaurantData>>> = flow {
        val result = api.filterRestaurantList(filter, location, radius)

        if(result.isSuccessful) {
            Log.d("TEST", "success")
            emit(BaseState.Success(result.body()!!.body.map { it.toDomainModel() }))
        }else {
            Log.d("TEST", "fail")
            emit(BaseState.Error(result.code(), result.message()))
        }
    }
}