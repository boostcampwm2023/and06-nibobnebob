package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.AddWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteMyWishRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.DeleteRestaurantUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.GetRestaurantDetailUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.PostLikeReviewUseCase
import com.avengers.nibobnebob.domain.usecase.restaurant.PostUnlikeReviewUseCase
import com.avengers.nibobnebob.presentation.ui.main.global.mapper.toUiRestaurantDetailInfo
import com.avengers.nibobnebob.presentation.ui.main.global.mapper.toUiRestaurantReviewDataInfo
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_OLD
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

sealed class RestaurantDetailEvents {
    data object NavigateToBack : RestaurantDetailEvents()
    data class NavigateToAddMyList(val restaurantId: Int) : RestaurantDetailEvents()
    data class NavigateToDeleteMyList(val restaurantId: Int) : RestaurantDetailEvents()
    data class NavigateToReviewPage(
        val restaurantName: String,
        val restaurantId: Int
    ) : RestaurantDetailEvents()

    data class ShowSnackMessage(val msg: String) : RestaurantDetailEvents()
    data class ShowToastMessage(val msg: String) : RestaurantDetailEvents()
}

data class RestaurantDetailUiState(
    val restaurantId: Int = 0,
    val name: String = "",
    val address: String = "",
    val reviewCnt: String = "",
    val phoneNumber: String = "",
    val category: String = "",
    val isWish: Boolean = false,
    val isMy: Boolean = false,
    val reviewer: String = "",
    val reviewList: List<UiReviewData> = emptyList(),
    val reviewFilter: String = FILTER_OLD
)


@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val getRestaurantDetailUseCase: GetRestaurantDetailUseCase,
    private val deleteRestaurantUseCase: DeleteRestaurantUseCase,
    private val deleteMyWishRestaurantUseCase: DeleteMyWishRestaurantUseCase,
    private val addWishRestaurantUseCase: AddWishRestaurantUseCase,
    private val postLikeReviewUseCase: PostLikeReviewUseCase,
    private val postUnlikeReviewUseCase: PostUnlikeReviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantDetailEvents>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<RestaurantDetailEvents> = _events.asSharedFlow()

    val restaurantId = MutableStateFlow(0)

    fun restaurantDetail() {
        getRestaurantDetailUseCase(restaurantId = restaurantId.value).onEach {
            when (it) {
                is BaseState.Success -> {
                    it.data.toUiRestaurantDetailInfo(
                        ::onWishClicked
                    ).apply {
                        _uiState.update { state ->
                            state.copy(
                                name = name,
                                address = address,
                                reviewCnt = reviewCnt,
                                phoneNumber = phoneNumber,
                                category = category,
                                isWish = isWish,
                                isMy = isMy
                            )
                        }
                    }
                    it.data.reviews?.let { reviews ->
                        _uiState.update { state ->
                            state.copy(
                                reviewList = reviews.map { data ->
                                    data.toUiRestaurantReviewDataInfo(
                                        ::onThumbsUpItemClicked,
                                        ::onThumbsDownItemClicked,
                                    )
                                }
                            )
                        }
                    }
                }

                is BaseState.Error -> {
                    _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteMyList() {
        deleteRestaurantUseCase(restaurantId.value).onEach {
            when (it) {
                is BaseState.Success -> {
                    _events.emit(RestaurantDetailEvents.ShowToastMessage("삭제 되었습니다."))
                    _uiState.update { state ->
                        state.copy(
                            isMy = false
                        )
                    }
                }

                is BaseState.Error -> {
                    _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onWishClicked() {
        if (_uiState.value.isWish) {
            deleteMyWishRestaurantUseCase(restaurantId.value).onEach {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isWish = false
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            addWishRestaurantUseCase(restaurantId.value).onEach {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isWish = true
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onThumbsUpItemClicked(reviewId: Int) {
        postLikeReviewUseCase(reviewId).onEach {
            when (it) {
                is BaseState.Success -> {
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

                is BaseState.Error -> _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)

    }


    private fun onThumbsDownItemClicked(reviewId: Int) {
        postUnlikeReviewUseCase(reviewId).onEach {
            when (it) {
                is BaseState.Success -> {
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

                is BaseState.Error -> _events.emit(RestaurantDetailEvents.ShowSnackMessage(ERROR_MSG))
            }
        }.launchIn(viewModelScope)

    }


    fun onMyListClicked() {
        if (_uiState.value.isMy) {
            navigateToDeleteMyList()
        } else {
            navigateToAddMyList()
        }
    }


    fun setRestaurantId(id: Int) {
        restaurantId.value = id
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToBack)
        }
    }

    private fun navigateToDeleteMyList() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToDeleteMyList(restaurantId.value))
        }
    }

    private fun navigateToAddMyList() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToAddMyList(restaurantId.value))
        }
    }

    fun navigateToReviewPage() {
        viewModelScope.launch {
            _events.emit(
                RestaurantDetailEvents.NavigateToReviewPage(
                    uiState.value.name,
                    restaurantId.value
                )
            )
        }
    }
}