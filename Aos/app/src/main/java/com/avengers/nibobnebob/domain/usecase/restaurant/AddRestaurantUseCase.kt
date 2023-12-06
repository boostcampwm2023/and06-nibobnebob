package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.data.model.request.AddRestaurantRequest
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(restaurantId: Int, body: AddRestaurantRequest): Flow<BaseState<Unit>> =
        repository.addRestaurant(restaurantId, body)
}