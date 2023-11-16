package com.avengers.nibobnebob.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.app.NetworkManager
import com.avengers.nibobnebob.databinding.ActivitySplashBinding
import com.avengers.nibobnebob.presentation.base.BaseActivity
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var netWorkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        repeatOnStarted {
            delay(2000)
            viewModel.getAutoLogin()
        }
        repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is SplashViewModel.NavigationEvent.NavigateToIntro -> moveToIntroActivity()
                    is SplashViewModel.NavigationEvent.NavigateToMain -> moveToMainActivity()
                }
            }
        }

        repeatOnStarted {
            netWorkManager.isNetworkConnected.collect { connected ->
                if (connected.not()) {
                    noNetworkSnackBar()
                }
            }
        }
        netWorkManager.startNetwork()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        netWorkManager.endNetwork()
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

    fun noNetworkSnackBar() {
        Snackbar.make(
            binding.root,
            R.string.no_network_text,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            //네트워크 통신 관련 데이터통신 진행
        }.show()
    }
}