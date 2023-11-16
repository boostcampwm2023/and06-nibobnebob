package com.avengers.nibobnebob.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.avengers.nibobnebob.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<B : ViewDataBinding>(
    private val inflate: (LayoutInflater) -> B,
) : AppCompatActivity() {
    
    protected lateinit var binding: B
    protected abstract val activityViewModel : BaseActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }
    
    protected fun initNetworkStateObserver(){
        repeatOnStarted {
            activityViewModel.networkState.collect{
                when(it){
                    NetWorkState.NETWORK_DISCONNECTED -> noNetworkSnackBar()
                    NetWorkState.NETWORK_CONNECTED -> {
                        // todo 네트워크 연결 감지시 fra
                    } 
                    else -> {}
                }
            }
        }
    }

    private fun noNetworkSnackBar() {
        Snackbar.make(
            binding.root,
            R.string.no_network_text,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            //initViewData()
        }.show()
    }

}