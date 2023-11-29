package com.avengers.nibobnebob.presentation.ui.main.follow.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.repository.FollowRepository
import com.avengers.nibobnebob.presentation.ui.main.follow.mapper.toUiFollowSearchData
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowSearchData
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
import javax.inject.Inject


data class FollowSearchUiState(
    val searchList: List<UiFollowSearchData> = emptyList(),
    val curRegionFilter: List<String> = emptyList()
)

sealed class FollowSearchEvents{
    data class ShowSnackMessage(val msg: String): FollowSearchEvents()
}

@HiltViewModel
class FollowSearchViewModel @Inject constructor(
    private val followRepository: FollowRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(FollowSearchUiState())
    val uiState: StateFlow<FollowSearchUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FollowSearchEvents>()
    val events: SharedFlow<FollowSearchEvents> = _events.asSharedFlow()

    val keyword = MutableStateFlow("")

    init{
        observeKeyword()
    }

    private fun observeKeyword(){
        keyword.onEach {
            followRepository.searchFollow(it, _uiState.value.curRegionFilter).onEach { response ->
                when(response){
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                searchList = response.data.body.userInfo.map { data ->
                                    data.toUiFollowSearchData {

                                    }
                                }
                            )
                        }
                    }
                    is BaseState.Error -> {
                        _events.emit(FollowSearchEvents.ShowSnackMessage(response.message))
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


}