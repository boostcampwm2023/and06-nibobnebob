package com.avengers.nibobnebob.data.repository

import android.util.Log
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.RestaurantApi
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val api: RestaurantApi
) : RestaurantRepository {

    override fun restaurantDetail(restaurantId: Int): Flow<BaseState<RestaurantDetailData>> =
        flow {
            when (val result = runRemote { api.restaurantDetail(restaurantId) }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                    }
                }

                is BaseState.Error -> {
                    emit(result)
                }
            }
        }

    override fun addRestaurant(
        restaurantId: Int,
        body: AddRestaurantRequest
    ): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.addRestaurant(restaurantId, body) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun deleteRestaurant(restaurantId: Int): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.deleteRestaurant(restaurantId) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun myRestaurantList(): Flow<BaseState<RestaurantData>> =
        flow {
            when (val result = runRemote { api.myRestaurantList() }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                        Log.d("test", "널 체크")
                    }
                }

                is BaseState.Error -> {
                    emit(result)
                    Log.d("test", "에러 체크")
                }
            }
        }

    override fun myWishList(): Flow<BaseState<WishRestaurantData>> =
        flow {
            when (val result = runRemote { api.myWishList() }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                        Log.d("test", "널 체크")
                    }
                }

                is BaseState.Error -> {
                    emit(result)
                    Log.d("test", "에러")
                }
            }
        }

    override fun addWishRestaurant(restaurantId: Int): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.addWishRestaurant(restaurantId) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun deleteWishRestaurant(restaurantId: Int): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.deleteWishRestaurant(restaurantId) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getRestaurantIsWish(id: Int): Flow<BaseState<RestaurantIsWishData>> =
        flow {
            when (val result = runRemote { api.getRestaurantIsWish(id) }) {
                is BaseState.Success -> {
                    result.data.body?.let { body ->
                        emit(BaseState.Success(body.toDomainModel()))
                    } ?: run {
                        emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                        Log.d("test", "널 체크")
                    }
                }

                is BaseState.Error -> {
                    emit(result)
                    Log.d("test", "에러 체크")
                }
            }
        }
}