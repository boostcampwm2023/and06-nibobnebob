package com.avengers.nibobnebob.presentation.ui.main.global.restaurantreview

import androidx.lifecycle.ViewModel
import com.avengers.nibobnebob.domain.usecase.restaurant.GetSortedReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantReviewsViewModel @Inject constructor(
    private val getSortedReviewUseCase: GetSortedReviewUseCase
) : ViewModel() {

    fun sortReview(
        sort: String?
    ) {
//        getSortedReviewUseCase(restaurantId.value, sort).onEach { review ->
//            when (review) {
//                is BaseState.Success -> {
//                    _uiState.update { state ->
//                        state.copy(
//                            reviewList = review.data.reviewItems.map {
//                                it.toUiRestaurantReviewDataInfo(
//                                    ::onThumbsUpItemClicked,
//                                    ::onThumbsDownItemClicked,
//                                )
//                            },
//                            reviewFilter = sort ?: Constants.FILTER_OLD
//                        )
//                    }
//                }
//
//                else -> _events.emit(RestaurantDetailEvents.ShowSnackMessage(Constants.ERROR_MSG))
//            }
//        }.launchIn(viewModelScope)
    }
}