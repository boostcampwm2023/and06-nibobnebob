package com.avengers.nibobnebob.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        repeatOnStarted {
            delay(2000)
            viewModel.getAutoLogin()
        }
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is SplashViewModel.NavigationEvent.NavigateToIntro -> moveToIntroActivity()
                    is SplashViewModel.NavigationEvent.NavigateToMain -> moveToMainActivity()
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }
}