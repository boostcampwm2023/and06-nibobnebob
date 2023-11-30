package com.avengers.nibobnebob.presentation.ui.intro


import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val networkManager: NetworkManager
) : BaseActivityViewModel(networkManager) {

}