package com.avengers.nibobnebob.presentation.ui.intro


import androidx.lifecycle.viewModelScope
import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _image = MutableStateFlow("")
    val image: StateFlow<String> = _image.asStateFlow()


    fun openGallery(){
        viewModelScope.launch {
            _events.emit(IntroEvents.OpenGallery)
        }
    }

    fun setUriString(uri : String){
        _image.value = uri
    }

    fun uriCollected(){
        _image.value = ""
    }
}