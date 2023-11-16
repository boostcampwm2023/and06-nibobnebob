package com.avengers.nibobnebob.presentation.ui.intro

import android.os.Bundle
import androidx.activity.viewModels
import com.avengers.nibobnebob.databinding.ActivityIntroBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    override val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}