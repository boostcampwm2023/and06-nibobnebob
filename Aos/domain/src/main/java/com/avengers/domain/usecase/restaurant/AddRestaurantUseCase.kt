package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        overallExperience:RequestBody,
        reviewImage: MultipartBody.Part?
    ): Flow<BaseState<Unit>> =
        repository.addRestaurant(
            restaurantId,
            isCarVisit,
            transportationAccessibility,
            parkingArea,
            taste,
            service,
            restroomCleanliness,
            overallExperience,
            reviewImage
        )
}