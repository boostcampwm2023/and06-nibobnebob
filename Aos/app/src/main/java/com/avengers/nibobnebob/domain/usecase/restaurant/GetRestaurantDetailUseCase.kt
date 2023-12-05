package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantDetailUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(restaurantId: Int): Flow<BaseState<RestaurantDetailData>> =
        repository.restaurantDetail(restaurantId)

}