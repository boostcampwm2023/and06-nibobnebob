package com.avengers.nibobnebob.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.avengers.nibobnebob.databinding.ActivitySplashBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2000)
            viewModel.getAutoLogin()
        }

        lifecycleScope.launch {
            viewModel.eventFlow.collect{
                when(it){
                    is SplashViewModel.NavigationEvent.NavigateToIntro -> moveToIntroActivity()
                    is SplashViewModel.NavigationEvent.NavigateToMain -> moveToMainActivity()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
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