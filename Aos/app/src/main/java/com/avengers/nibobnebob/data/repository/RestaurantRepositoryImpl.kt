package com.avengers.nibobnebob.data.repository

import android.util.Log
import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.data.model.response.MyRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantDetailResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantIsWishResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.RestaurantItemResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.ReviewSortResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.WishRestaurantResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.RestaurantApi
import com.avengers.nibobnebob.domain.model.MyRestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.avengers.nibobnebob.domain.model.ReviewSortData
import com.avengers.nibobnebob.domain.model.SearchRestaurantData
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

    override fun sortReview(restaurantId: Int, sort: String?): Flow<BaseState<ReviewSortData>> =
        flow {
            when (val result = runRemote { api.sortReview(restaurantId, sort) }) {
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
        overallExperience: String
    ): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote {
            api.addRestaurant(
                restaurantId, AddRestaurantRequest(
                    isCarVisit,
                    transportationAccessibility,
                    parkingArea,
                    taste,
                    service,
                    restroomCleanliness,
                    overallExperience
                )
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

    override fun myRestaurantList(
        limit: Int?,
        page: Int?,
        sort: String?
    ): Flow<BaseState<MyRestaurantData>> =
        flow {
            when (val result = runRemote { api.myRestaurantList(limit, page, sort) }) {
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

    override fun myWishList(
        limit: Int?,
        page: Int?,
        sort: String?
    ): Flow<BaseState<WishRestaurantData>> =
        flow {
            when (val result = runRemote { api.myWishList(limit, page, sort) }) {
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

    override fun likeReview(reviewId: Int): Flow<BaseState<Unit>> = flow {
        when (val result = runRemote { api.likeReview(reviewId) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun unlikeReview(reviewId: Int): Flow<BaseState<Unit>> = flow{
        when (val result = runRemote { api.unlikeReview(reviewId) }) {
            is BaseState.Success -> {
                emit(BaseState.Success(Unit))
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }
}