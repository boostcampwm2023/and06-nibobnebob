package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.RestaurantIsWishData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRestaurantIsWishUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(id: Int): Flow<BaseState<RestaurantIsWishData>> =
        repository.getRestaurantIsWish(id)
}