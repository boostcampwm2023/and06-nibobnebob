package com.avengers.nibobnebob.presentation.ui.intro.splash

import android.os.Bundle
import com.avengers.nibobnebob.databinding.ActivitySplashBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}