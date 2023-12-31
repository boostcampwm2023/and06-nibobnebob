package com.avengers.nibobnebob.presentation.ui.main.global.restaurantadd

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.usecase.restaurant.AddRestaurantUseCase
import com.avengers.nibobnebob.presentation.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class AddMyRestaurantUiState(
    val restaurantName: String = "",
    val visitWithCar: Boolean = false,
    val parkingSpace: Int = 0,
    val traffic: Int = 0,
    val taste: Int = 0,
    val service: Int = 0,
    val toilet: Int = 0,
    val commentState: CommentState = CommentState.Empty
)

sealed class CommentState {
    data class Over(val msg: String) : CommentState()
    data class Lack(val msg: String) : CommentState()
    data class Success(val msg: String) : CommentState()
    data object Empty : CommentState()
}

sealed class AddMyRestaurantEvents {
    data object NavigateToBack : AddMyRestaurantEvents()
    data object ShowConfirmDialog : AddMyRestaurantEvents()
    data object ShowSuccessDialog : AddMyRestaurantEvents()
    data class ShowToastMessage(val msg: String) : AddMyRestaurantEvents()
    data class ShowSnackMessage(val msg: String) : AddMyRestaurantEvents()
    data object OpenGallery : AddMyRestaurantEvents()
    data object ShowLoading : AddMyRestaurantEvents()
    data object DismissLoading : AddMyRestaurantEvents()
}

@HiltViewModel
class AddMyRestaurantViewModel @Inject constructor(
    private val addRestaurantUseCase: AddRestaurantUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMyRestaurantUiState())
    val uiState: StateFlow<AddMyRestaurantUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddMyRestaurantEvents>()
    val events: SharedFlow<AddMyRestaurantEvents> = _events.asSharedFlow()

    val comment = MutableStateFlow("")
    val isDataReady = MutableStateFlow(false)

    val reviewImg = MutableStateFlow("")
    private var reviewImgFile: MultipartBody.Part? = null
    private var restaurantId = -1

    init {
        observeComment()
    }

    private fun observeComment() {
        comment.onEach {
            when (it.length) {
                in 0..19 -> {
                    _uiState.update { state ->
                        state.copy(commentState = CommentState.Lack("20자 이상 작성하셔야 합니다!"))
                    }
                    isDataReady.value = false
                }

                in 20..200 -> {
                    _uiState.update { state ->
                        state.copy(commentState = CommentState.Success("적절한 길이의 리뷰 입니다!"))
                    }
                    isDataReady.value = true
                }

                else -> {
                    _uiState.update { state ->
                        state.copy(commentState = CommentState.Over("200자 이하로 작성하셔야 합니다!"))
                    }
                    isDataReady.value = false
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setIsVisitWithCar(visitWithCar: Boolean) {
        _uiState.update { state ->
            state.copy(visitWithCar = visitWithCar)
        }
    }

    fun sliderStateChange(
        estimateItem: EstimateItem,
        value: Int
    ) {
        when (estimateItem) {
            EstimateItem.PARKING -> {
                _uiState.update { state ->
                    state.copy(parkingSpace = value)
                }
            }

            EstimateItem.TRAFFIC -> {
                _uiState.update { state ->
                    state.copy(traffic = value)
                }
            }

            EstimateItem.TASTE -> {
                _uiState.update { state ->
                    state.copy(taste = value)
                }
            }

            EstimateItem.SERVICE -> {
                _uiState.update { state ->
                    state.copy(service = value)
                }
            }

            EstimateItem.TOILET -> {
                _uiState.update { state ->
                    state.copy(toilet = value)
                }
            }
        }
    }

    fun showConfirmDialog() {
        viewModelScope.launch {
            _events.emit(AddMyRestaurantEvents.ShowConfirmDialog)
        }
    }

    fun addReview() {
        Log.d("debugging", reviewImgFile.toString())
        addRestaurantUseCase(
            restaurantId = restaurantId,
            isCarVisit = _uiState.value.visitWithCar,
            transportationAccessibility = if (_uiState.value.visitWithCar) null else _uiState.value.traffic,
            parkingArea = if (_uiState.value.visitWithCar) _uiState.value.parkingSpace else null,
            taste = _uiState.value.taste,
            service = _uiState.value.taste,
            restroomCleanliness = _uiState.value.toilet,
            overallExperience = comment.value.toRequestBody("text/plain".toMediaTypeOrNull()),
            reviewImage = reviewImgFile
        ).onStart {
            _events.emit(AddMyRestaurantEvents.ShowLoading)
        }.onEach { state ->
            when (state) {
                is BaseState.Success -> {
                    _events.emit(AddMyRestaurantEvents.ShowSuccessDialog)
                    _events.emit(AddMyRestaurantEvents.ShowToastMessage("내 맛집 리스트에 추가되었습니다."))
                }

                is BaseState.Error -> _events.emit(
                    AddMyRestaurantEvents.ShowSnackMessage(ERROR_MSG)
                )
            }
        }.onCompletion {
            _events.emit(AddMyRestaurantEvents.DismissLoading)
        }.launchIn(viewModelScope)
    }


    fun setDefaultValue(
        name: String,
        id: Int
    ) {
        _uiState.update { state ->
            state.copy(restaurantName = name)
        }
        restaurantId = id
    }

    fun openGallery() {
        viewModelScope.launch {
            _events.emit(AddMyRestaurantEvents.OpenGallery)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(AddMyRestaurantEvents.NavigateToBack)
        }
    }

    fun setImage(uri: String, file: MultipartBody.Part) {
        reviewImg.value = uri
        reviewImgFile = file
    }
}

enum class EstimateItem() {
    PARKING,
    TRAFFIC,
    TASTE,
    SERVICE,
    TOILET,
}