package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.GlobalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GlobalRepositoryImpl @Inject constructor(
    private val api: GlobalApi
) : GlobalRepository {

    override fun restaurantDetail(restaurantId: Int): Flow<BaseState<BaseResponse<RestaurantDetailResponse>>> = flow{
        val result = runRemote {api.restaurantDetail(restaurantId)}
        emit(result)
    }
}