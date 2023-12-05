package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyRestaurantListUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(): Flow<BaseState<List<RestaurantData>>> = repository.myRestaurantList()
}