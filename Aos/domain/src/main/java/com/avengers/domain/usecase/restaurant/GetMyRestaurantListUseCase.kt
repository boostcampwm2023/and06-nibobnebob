package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.MyRestaurantData
import com.avengers.nibobnebob.domain.model.MyRestaurantItemData
import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyRestaurantListUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(
        longitude: String? = null,
        latitude: String? = null,
        limit: Int? = null,
        page: Int? = null,
        sort: String? = null,
    ): Flow<BaseState<MyRestaurantData>> =
        repository.myRestaurantList(longitude, latitude, limit, page, sort)
}