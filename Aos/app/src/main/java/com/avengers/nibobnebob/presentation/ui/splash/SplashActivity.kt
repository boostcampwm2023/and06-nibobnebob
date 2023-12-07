package com.avengers.nibobnebob.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import com.avengers.nibobnebob.databinding.ActivitySplashBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override val viewModel: SplashViewModel by viewModels()

    override fun initView() {
        binding.vm = viewModel
    }

    override fun initEventObserver() {
        repeatOnStarted {
            delay(2000)
            viewModel.getAutoLogin()
        }
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is SplashUiEvent.NavigateToIntro -> toIntroActivity()
                    is SplashUiEvent.NavigateToMain -> toMainActivity()
                }
            }
        }
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun toIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}