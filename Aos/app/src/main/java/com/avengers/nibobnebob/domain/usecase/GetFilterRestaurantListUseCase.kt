package com.avengers.nibobnebob.domain.usecase

import com.avengers.nibobnebob.domain.model.RestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFilterRestaurantListUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(
        filter: String,
        location: String,
        radius: Int
    ) : Flow<BaseState<List<RestaurantData>>> = flow {
        repository.filterRestaurantList(filter, location, radius)
    }
}