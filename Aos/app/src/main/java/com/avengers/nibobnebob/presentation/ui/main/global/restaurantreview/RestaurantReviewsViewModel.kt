package com.avengers.nibobnebob.presentation.ui.main.global.restaurantreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.GetSortedReviewUseCase
import com.avengers.nibobnebob.presentation.ui.main.global.mapper.toUiRestaurantReviewDataInfo
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData
import com.avengers.nibobnebob.presentation.util.Constants
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
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
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RestaurantReviewsEvents {
    data object NavigateToBack : RestaurantReviewsEvents()
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
            limit = ITEM_LIMIT,
            page = FIRST_PAGE,
            sort = sort ?: uiState.value.reviewFilter
        ).onEach { review ->
            when (review) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            restaurantId = id,
                            name = name,
                            listPage = 2,
                            lastPage = review.data.hasNext,
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

    fun loadNextPage() {
        _uiState.update { it.copy(isLoading = true) }
        getSortedReviewUseCase(
            id = uiState.value.restaurantId,
            limit = ITEM_LIMIT,
            page = uiState.value.listPage,
            sort = uiState.value.reviewFilter
        ).onEach { review ->
            when (review) {
                is BaseState.Success -> {
                    _uiState.update { state ->
                        val updateList = uiState.value.reviewList.toMutableList().apply {
                            addAll(review.data.reviewItems.map {
                                it.toUiRestaurantReviewDataInfo(
                                    ::onThumbsUpItemClicked,
                                    ::onThumbsDownItemClicked,
                                )
                            })
                        }
                        state.copy(
                            reviewList = updateList,
                            listPage = uiState.value.listPage + 1,
                            lastPage = review.data.hasNext,
                            isLoading = false
                        )
                    }
                }

                else -> _events.emit(RestaurantReviewsEvents.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)
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

    fun navigateToBack() {
        viewModelScope.launch { _events.emit(RestaurantReviewsEvents.NavigateToBack) }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val ITEM_LIMIT = 3
    }
}