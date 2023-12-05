package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.RestaurantApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val api: RestaurantApi
) : RestaurantRepository {

    override fun restaurantDetail(restaurantId: Int): Flow<BaseState<BaseResponse<RestaurantDetailResponse>>> =
        flow {
            val result = runRemote { api.restaurantDetail(restaurantId) }
            emit(result)
        }

    override fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<BaseState<BaseResponse<Unit>>> =
        flow {
            val result = runRemote { api.addRestaurant(restaurantId, body) }
            emit(result)
        }

    override fun deleteRestaurant(restaurantId: Int): Flow<BaseState<BaseResponse<Unit>>> = flow {
        val result = runRemote { api.deleteRestaurant(restaurantId) }
        emit(result)
    }


    override fun myRestaurantList(): Flow<BaseState<BaseResponse<List<RestaurantResponse>>>> =
        flow {
            val result = runRemote { api.myRestaurantList() }
            emit(result)
        }

    override fun myWishList(): Flow<BaseState<BaseResponse<List<WishRestaurantResponse>>>> =
        flow {
            val result = runRemote { api.myWishList() }
            emit(result)
        }

    override fun addWishRestaurant(restaurantId: Int): Flow<BaseState<BaseResponse<Unit>>> = flow {
        val result = runRemote { api.addWishRestaurant(restaurantId) }
        emit(result)
    }

    override fun deleteWishRestaurant(restaurantId: Int): Flow<BaseState<BaseResponse<Unit>>> =
        flow {
            val result = runRemote { api.deleteWishRestaurant(restaurantId) }
            emit(result)
        }

    override fun getRestaurantIsWish(id: Int): Flow<BaseState<BaseResponse<RestaurantIsWishResponse>>> =
        flow {
            val result = runRemote { api.getRestaurantIsWish(id) }
            emit(result)
        }
}