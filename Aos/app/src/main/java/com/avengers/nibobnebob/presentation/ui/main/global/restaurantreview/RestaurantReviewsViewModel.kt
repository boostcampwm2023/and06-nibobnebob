package com.avengers.nibobnebob.presentation.ui.main.global.restaurantreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.GetSortedReviewUseCase
import com.avengers.nibobnebob.presentation.ui.main.global.mapper.toUiRestaurantReviewDataInfo
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData
import com.avengers.nibobnebob.presentation.util.Constants
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_NEW
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class RestaurantReviewsEvents {
    data class ShowSnackMessage(val msg: String) : RestaurantReviewsEvents()
    data class ShowToastMessage(val msg: String) : RestaurantReviewsEvents()
}

data class RestaurantReviewsUiState(
    val restaurantId: Int = 0,
    val name: String = "",
    val reviewList: List<UiReviewData> = emptyList(),
    val listPage: Int = 1,
    val lastPage: Boolean = false,
    val isLoading: Boolean = false,
    val reviewFilter: String = FILTER_NEW
)

@HiltViewModel
class RestaurantReviewsViewModel @Inject constructor(
    private val getSortedReviewUseCase: GetSortedReviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantReviewsUiState())
    val uiState: StateFlow<RestaurantReviewsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantReviewsEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<RestaurantReviewsEvents> = _events.asSharedFlow()

    fun getAllReviews(id: Int, name: String, sort: String? = null) {
        getSortedReviewUseCase(
            id,
            limit = 2,
            page = 1,
            sort = sort ?: uiState.value.reviewFilter
        ).onEach { review ->
            when (review) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            restaurantId = id,
                            name = name,
                            listPage = 2,
                            reviewFilter = sort ?: uiState.value.reviewFilter,
                            reviewList = review.data.reviewItems.map {
                                it.toUiRestaurantReviewDataInfo(
                                    ::onThumbsUpItemClicked,
                                    ::onThumbsDownItemClicked,
                                )
                            }
                        )
                    }
                }

                else -> _events.emit(RestaurantReviewsEvents.ShowSnackMessage(Constants.ERROR_MSG))
            }
        }.launchIn(viewModelScope)
    }

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

    //TODO : 추후 서버 좋아요 진행
    private fun onThumbsUpItemClicked(reviewId: Int) {
        _uiState.update { state ->
            val updatedReviewList = state.reviewList.map { existingReviewData ->
                if (existingReviewData.reviewId == reviewId) {
                    if (existingReviewData.isThumbsUp) {
                        existingReviewData.copy(
                            thumbsUpCnt = existingReviewData.thumbsUpCnt - 1,
                            isThumbsUp = false
                        )
                    } else {
                        if (existingReviewData.isThumbsDown) {
                            existingReviewData.copy(
                                thumbsUpCnt = existingReviewData.thumbsUpCnt + 1,
                                isThumbsUp = true,
                                thumbsDownCnt = existingReviewData.thumbsDownCnt - 1,
                                isThumbsDown = false
                            )
                        } else {
                            existingReviewData.copy(
                                thumbsUpCnt = existingReviewData.thumbsUpCnt + 1,
                                isThumbsUp = true
                            )
                        }
                    }
                } else {
                    existingReviewData
                }
            }
            state.copy(reviewList = updatedReviewList)
        }
    }


    //TODO : 추후 서버 싫어요 진행
    private fun onThumbsDownItemClicked(reviewId: Int) {
        _uiState.update { state ->
            val updatedReviewList = state.reviewList.map { existingReviewData ->
                if (existingReviewData.reviewId == reviewId) {
                    if (existingReviewData.isThumbsDown) {
                        existingReviewData.copy(
                            thumbsDownCnt = existingReviewData.thumbsDownCnt - 1,
                            isThumbsDown = false
                        )
                    } else {
                        if (existingReviewData.isThumbsUp) {
                            existingReviewData.copy(
                                thumbsDownCnt = existingReviewData.thumbsDownCnt + 1,
                                isThumbsDown = true,
                                thumbsUpCnt = existingReviewData.thumbsUpCnt - 1,
                                isThumbsUp = false
                            )
                        } else {
                            existingReviewData.copy(
                                thumbsDownCnt = existingReviewData.thumbsDownCnt + 1,
                                isThumbsDown = true
                            )
                        }
                    }
                } else {
                    existingReviewData
                }
            }
            state.copy(reviewList = updatedReviewList)
        }
    }
}