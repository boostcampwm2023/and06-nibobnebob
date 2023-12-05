package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.RestaurantResponse
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse
import com.avengers.nibobnebob.data.model.oldRunRemote
import com.avengers.nibobnebob.data.remote.RestaurantApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val api: RestaurantApi
) : RestaurantRepository {

    override fun restaurantDetail(restaurantId: Int): Flow<OldBaseState<OldBaseResponse<RestaurantDetailResponse>>> =
        flow {
            val result = oldRunRemote { api.restaurantDetail(restaurantId) }
            emit(result)
        }

    override fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<OldBaseState<OldBaseResponse<Unit>>> =
        flow {
            val result = oldRunRemote { api.addRestaurant(restaurantId, body) }
            emit(result)
        }

    override fun deleteRestaurant(restaurantId: Int): Flow<OldBaseState<OldBaseResponse<Unit>>> = flow {
        val result = oldRunRemote { api.deleteRestaurant(restaurantId) }
        emit(result)
    }


    override fun myRestaurantList(): Flow<OldBaseState<OldBaseResponse<List<RestaurantResponse>>>> =
        flow {
            val result = oldRunRemote { api.myRestaurantList() }
            emit(result)
        }

    override fun myWishList(): Flow<OldBaseState<OldBaseResponse<List<WishRestaurantResponse>>>> =
        flow {
            val result = oldRunRemote { api.myWishList() }
            emit(result)
        }

    override fun addWishRestaurant(restaurantId: Int): Flow<OldBaseState<OldBaseResponse<Unit>>> = flow {
        val result = oldRunRemote { api.addWishRestaurant(restaurantId) }
        emit(result)
    }

    override fun deleteWishRestaurant(restaurantId: Int): Flow<OldBaseState<OldBaseResponse<Unit>>> =
        flow {
            val result = oldRunRemote { api.deleteWishRestaurant(restaurantId) }
            emit(result)
        }

    override fun getRestaurantIsWish(id: Int): Flow<OldBaseState<OldBaseResponse<RestaurantIsWishResponse>>> =
        flow {
            val result = oldRunRemote { api.getRestaurantIsWish(id) }
            emit(result)
        }
}