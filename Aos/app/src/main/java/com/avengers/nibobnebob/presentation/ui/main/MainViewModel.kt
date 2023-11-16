package com.avengers.nibobnebob.presentation.ui.main

import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkManager: NetworkManager
) : BaseActivityViewModel(networkManager) {


}