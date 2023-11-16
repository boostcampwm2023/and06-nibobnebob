package com.avengers.nibobnebob.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.app.NetWorkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    @Inject
    lateinit var netWorkManager: NetWorkManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        //initViewData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repeatOnStarted {
            netWorkManager.isNetworkConnected.collect { connected ->
                if (connected.not()) {
                    noNetworkSnackBar()
                }
            }
        }
    }

    //protected abstract fun initViewData()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }

    }

    override fun onResume() {
        super.onResume()
        netWorkManager.startNetwork()
    }

    override fun onPause() {
        super.onPause()
        netWorkManager.endNetwork()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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