package com.avengers.nibobnebob.domain.usecase.restaurant

import com.avengers.nibobnebob.domain.model.WishRestaurantData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyWishListUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(
        limit: Int? = null,
        page: Int? = null,
        sort: String? = null,
    ): Flow<BaseState<WishRestaurantData>> = repository.myWishList(limit, page, sort)
}