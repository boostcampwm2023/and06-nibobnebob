package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(
        restaurantId: Int, isCarVisit: Boolean,
        transportationAccessibility: Int?,
        parkingArea: Int?,
        taste: Int,
        service: Int,
        restroomCleanliness: Int,
        overallExperience: String
    ): Flow<BaseState<Unit>> =
        repository.addRestaurant(
            restaurantId,
            isCarVisit,
            transportationAccessibility,
            parkingArea,
            taste,
            service,
            restroomCleanliness,
            overallExperience
        )
}