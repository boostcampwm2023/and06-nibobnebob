package com.avengers.nibobnebob.presentation.ui.global.restaurantdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.GlobalRepository
import com.avengers.nibobnebob.presentation.ui.global.mapper.toUiRestaurantDetailInfo
import com.avengers.nibobnebob.presentation.ui.global.model.UiReviewData
import dagger.hilt.android.lifecycle.HiltViewModel
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
    data class NavigateToDetailReview(val reviewId : Int) : RestaurantDetailEvents()
}

data class RestaurantDetailUiState(
    val name : String = "",
    val address : String = "",
    val reviewCnt : Int = 0,
    val phoneNumber : String = "",
    val category : String = "",
    val isWish : Boolean = false,
    val reviewList : List<UiReviewData> = emptyList()
)


@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val globalRepository: GlobalRepository
) : ViewModel() {
    private val TAG = "RestaurantDetailViewModelDebug"

    private val _uiState = MutableStateFlow(RestaurantDetailUiState())
    val uiState: StateFlow<RestaurantDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RestaurantDetailEvents>()
    val events: SharedFlow<RestaurantDetailEvents> = _events.asSharedFlow()

    private val restaurantId = MutableStateFlow<Int>(1)


    fun restaurantDetail(){
        globalRepository.restaurantDetail(restaurantId = restaurantId.value).onEach {
            when(it){
                is BaseState.Success -> {
                    it.data.body.toUiRestaurantDetailInfo().apply {
                        _uiState.update { state ->
                            state.copy(
                                name = name,
                                address = address,
                                reviewCnt = reviewCnt,
                                phoneNumber = phoneNumber,
                                category = category,
                                isWish = false //TODO :받아오면 수정
                            )
                        }
                    }
                }
                is BaseState.Error -> {
                    Log.d(TAG,it.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateWish(){
        _uiState.update { state ->
            if(state.isWish)
                state.copy(isWish = false)
            else
                state.copy(isWish = true)
        }
    }

    fun getReviewList(){
        _uiState.update { state ->
            state.copy(
                reviewList = listOf(
                    UiReviewData(
                        reviewId = 1,
                        profileImage = "",
                        nickName = "노균욱",
                        visited = "2023-11-25",
                        parkingConvenience = 4,
                        taste = 5,
                        service = 3,
                        cleanliness = 4,
                        review = "이 음식점은 정말 맛집 중의 맛집입니다! 특히 메뉴 하나하나에 정성이 느껴져서 놀라웠어요. 다음에 또 방문할게요",
                        thumbsUpCnt = 1230,
                        thumbsDownCnt = 125,
                        isThumbsUp = true,
                        isThumbsDown = false,
                        onThumbsUpClick = ::onThumbsUpItemClicked,
                        onThumbsDownClick = ::onThumbsDownItemClicked,
                        onReviewClick = ::onReviewClicked
                    ),
                    UiReviewData(
                        reviewId = 2,
                        profileImage = "",
                        nickName = "박진성",
                        visited = "2023-11-25",
                        parkingConvenience = 3,
                        taste = 4,
                        service = 5,
                        cleanliness = 2,
                        review = "이 식당은 분위기가 너무 좋아서 친구들과 함께 오기에 딱이에요. 음식 맛도 훌륭하고 직원들도 친절해요.",
                        thumbsUpCnt = 12,
                        thumbsDownCnt = 12,
                        isThumbsUp = true,
                        isThumbsDown = false,
                        onThumbsUpClick = ::onThumbsUpItemClicked,
                        onThumbsDownClick = ::onThumbsDownItemClicked,
                        onReviewClick = ::onReviewClicked
                    ),
                    UiReviewData(
                        reviewId = 3,
                        profileImage = "",
                        nickName = "오세영",
                        visited = "2023-11-25",
                        parkingConvenience = 5,
                        taste = 3,
                        service = 4,
                        cleanliness = 1,
                        review = "\"이 음식점의 메뉴 다양성이 정말 매력적이에요. 맛있는 음식을 골고루 즐길 수 있어서 좋아합니다",
                        thumbsUpCnt = 10,
                        thumbsDownCnt = 15,
                        isThumbsUp = false,
                        isThumbsDown = false,
                        onThumbsUpClick = ::onThumbsUpItemClicked,
                        onThumbsDownClick = ::onThumbsDownItemClicked,
                        onReviewClick = ::onReviewClicked
                    ),
                    UiReviewData(
                        reviewId = 4,
                        profileImage = "",
                        nickName = "최근혁",
                        visited = "2023-11-25",
                        parkingConvenience = 2,
                        taste = 5,
                        service = 1,
                        cleanliness = 3,
                        review = "우리 가족이 함께 방문했는데, 모두가 만족했어요. 특히 청결한 식당과 맛있는 음식에 감동받았습니다.",
                        thumbsUpCnt = 8,
                        thumbsDownCnt = 7,
                        isThumbsUp = false,
                        isThumbsDown = true,
                        onThumbsUpClick = ::onThumbsUpItemClicked,
                        onThumbsDownClick = ::onThumbsDownItemClicked,
                        onReviewClick = ::onReviewClicked
                    ),
                    UiReviewData(
                        reviewId = 5,
                        profileImage = "",
                        nickName = "이태훈",
                        visited = "2023-11-25",
                        parkingConvenience = 1,
                        taste = 2,
                        service = 4,
                        cleanliness = 5,
                        review = "이 식당은 분위기가 너무 좋아서 친구들과 함께 오기에 딱이에요. 음식 맛도 훌륭하고 직원들도 친절해요.",
                        thumbsUpCnt = 5,
                        thumbsDownCnt = 3,
                        isThumbsUp = false,
                        isThumbsDown = true,
                        onThumbsUpClick = ::onThumbsUpItemClicked,
                        onThumbsDownClick = ::onThumbsDownItemClicked,
                        onReviewClick = ::onReviewClicked
                    )
                )
            )
        }
    }

    private fun onThumbsUpItemClicked(reviewData: UiReviewData) {
        _uiState.update { state ->
            val updatedReviewList = state.reviewList.map { existingReviewData ->
                if (existingReviewData == reviewData) {
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


    private fun onThumbsDownItemClicked(reviewData: UiReviewData) {
        _uiState.update { state ->
            val updatedReviewList = state.reviewList.map { existingReviewData ->
                if (existingReviewData == reviewData) {
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



    private fun onReviewClicked(reviewId: Int){
        navigateToRestaurantDetail(reviewId = reviewId)
    }



    fun setRestaurantId(id : Int){
        restaurantId.value = id
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToBack)
        }
    }

    private fun navigateToRestaurantDetail(reviewId : Int) {
        viewModelScope.launch {
            _events.emit(RestaurantDetailEvents.NavigateToDetailReview(reviewId = reviewId))
        }
    }
}