package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.ReviewSortData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSortedReviewUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(id: Int, sort: String? = null): Flow<BaseState<ReviewSortData>> =
        repository.sortReview(id, sort)
}