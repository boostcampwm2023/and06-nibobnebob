package com.avengers.nibobnebob.presentation.ui.intro


import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class IntroEvents{
    data object OpenGallery: IntroEvents()
}

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val networkManager: NetworkManager
) : BaseActivityViewModel(networkManager) {

    private val _events = MutableSharedFlow<IntroEvents>()
    val events: SharedFlow<IntroEvents> = _events.asSharedFlow()






    fun openGallery(){
        viewModelScope.launch {
            _events.emit(IntroEvents.OpenGallery)
        }
    }
}