package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.MyRestaurantData
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.RestaurantItemsData
import com.avengers.nibobnebob.domain.model.SearchRestaurantData
import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun restaurantDetail(
        restaurantId: Int
    ): Flow<BaseState<RestaurantDetailData>>

    fun addRestaurant(
        restaurantId: Int, isCarVisit: Boolean,
        transportationAccessibility: Int?,
        parkingArea: Int?,
        taste: Int,
        service: Int,
        restroomCleanliness: Int,
        overallExperience: String
    ): Flow<BaseState<Unit>>

    fun deleteRestaurant(
        restaurantId: Int,
    ): Flow<BaseState<Unit>>

    fun myRestaurantList(
        limit : Int? = null,
        page : Int? = null,
        sort : String? = null
    ): Flow<BaseState<MyRestaurantData>>

    fun myWishList(): Flow<BaseState<WishRestaurantData>>

    fun addWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<Unit>>

    fun deleteWishRestaurant(
        restaurantId: Int
    ): Flow<BaseState<Unit>>

    fun getRestaurantIsWish(
        id: Int
    ): Flow<BaseState<RestaurantIsWishData>>

    fun searchRestaurant(
        name: String,
        radius: String?,
        longitude: String?,
        latitude: String?
    ): Flow<BaseState<List<SearchRestaurantData>>>

    fun filterRestaurantList(
        filter: String,
        location: String,
        radius: Int
    ): Flow<BaseState<List<RestaurantItemsData>>>

    fun nearRestaurantList(
        radius: String,
        longitude: String,
        latitude: String,
    ): Flow<BaseState<List<RestaurantItemsData>>>
}