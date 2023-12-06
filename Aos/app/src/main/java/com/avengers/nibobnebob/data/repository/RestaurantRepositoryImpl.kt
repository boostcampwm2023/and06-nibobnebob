package com.avengers.nibobnebob.data.repository

import android.util.Log
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantItems.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.RestaurantApi
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.avengers.nibobnebob.domain.model.SearchRestaurantData
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        isCarVisit: Boolean,
        transportationAccessibility: Int?,
        parkingArea: Int?,
        taste: Int,
        service: Int,
        restroomCleanliness: Int,
        overallExperience: RequestBody,
        reviewImage: MultipartBody.Part?
    ): Flow<BaseState<Unit>> = flow {
        reviewImage?.let { image ->
            when (val result = runRemote {
                api.addRestaurant(
                    restaurantId,
                    isCarVisit,
                    transportationAccessibility,
                    parkingArea,
                    taste,
                    service,
                    restroomCleanliness,
                    overallExperience,
                    image
                )
            }) {
                is BaseState.Success -> {
                    emit(BaseState.Success(Unit))
                }

                is BaseState.Error -> {
                    emit(result)
                }
            }
        } ?: run {
            when (val result = runRemote {
                api.addRestaurantNoImage(
                    restaurantId,
                    isCarVisit,
                    transportationAccessibility,
                    parkingArea,
                    taste,
                    service,
                    restroomCleanliness,
                    overallExperience,
                )
            }) {
                is BaseState.Success -> {
                    emit(BaseState.Success(Unit))
                }

                is BaseState.Error -> {
                    emit(result)
                }
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
                    }
                }

                is BaseState.Error -> {
                    emit(result)
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
                    }
                }

                is BaseState.Error -> {
                    emit(result)
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
                    }
                }

                is BaseState.Error -> {
                    emit(result)
                }
            }
        }


    override fun searchRestaurant(
        name: String,
        radius: String?,
        longitude: String?,
        latitude: String?
    ): Flow<BaseState<List<SearchRestaurantData>>> = flow {
        when (val result = runRemote { api.searchRestaurant(name, radius, longitude, latitude) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<BaseState<List<RestaurantItemsData>>> = flow {

        when (val result = runRemote { api.filterRestaurantList(filter, location, radius) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    Log.d("test", "success")
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    Log.d("test", "fail")
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                Log.d("test", "error")
                emit(result)
            }
        }
    }

    override fun nearRestaurantList(
        radius: String,
        longitude: String,
        latitude: String
    ): Flow<BaseState<List<RestaurantItemsData>>> = flow {

        when (val result = runRemote { api.nearRestaurantList(radius, longitude, latitude) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }
}