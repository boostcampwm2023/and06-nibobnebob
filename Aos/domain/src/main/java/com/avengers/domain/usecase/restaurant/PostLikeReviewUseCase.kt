package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import javax.inject.Inject

class PostLikeReviewUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke(id: Int) = restaurantRepository.likeReview(id)
}