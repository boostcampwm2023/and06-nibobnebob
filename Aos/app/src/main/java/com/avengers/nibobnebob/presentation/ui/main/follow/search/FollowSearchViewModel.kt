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
import kotlinx.coroutines.launch
import javax.inject.Inject


data class FollowSearchUiState(
    val searchList: List<UiFollowSearchData> = emptyList(),
    val curRegionFilter: List<String> = emptyList(),
    val searchCount: String = ""
)

sealed class FollowSearchEvents{
    data class ShowSnackMessage(val msg: String): FollowSearchEvents()
    data class NavigateToUserDetail(val nickName: String): FollowSearchEvents()
    data class ShowFilterDialog(
        val curRegion: List<String>,
        val changeFilterListener: (List<String>) -> Unit
    ): FollowSearchEvents()
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
            if(it.isNotBlank()){

                followRepository.searchFollow(it, _uiState.value.curRegionFilter).onEach { response ->
                    when(response){
                        is BaseState.Success -> {
                            response.data.body.let{ data ->
                                _uiState.update { state ->
                                    state.copy(
                                        searchList = data.map { data ->
                                            data.toUiFollowSearchData(::navigateToUserDetail)
                                        },
                                        searchCount = "검색결과 ${data.size}건"
                                    )
                                }
                            }
                        }
                        is BaseState.Error -> {
                            _events.emit(FollowSearchEvents.ShowSnackMessage(response.message))
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }.launchIn(viewModelScope)
    }

    private fun navigateToUserDetail(nickName: String) {
        viewModelScope.launch {
            _events.emit(FollowSearchEvents.NavigateToUserDetail(nickName))
        }
    }

    fun showFilterDialog(){
        viewModelScope.launch {
            _events.emit(FollowSearchEvents.ShowFilterDialog(
                _uiState.value.curRegionFilter,
                ::changeFilterListener
            ))
        }
    }

    private fun changeFilterListener(newFilter: List<String>){
        _uiState.update { state ->
            state.copy(
                curRegionFilter = newFilter
            )
        }
    }



}