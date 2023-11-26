package com.avengers.nibobnebob.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.customview.TwoButtonDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val parentViewModel : BaseActivityViewModel

    private lateinit var twoButtonDialog: TwoButtonDialog
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnStarted {
            parentViewModel.networkState.collect{
                when(it){
                    NetWorkState.NETWORK_DISCONNECTED -> {
                        noNetworkSnackBar()
                    }
                    NetWorkState.NETWORK_CONNECTED -> {
                        // todo initNetworkView 실행
                    }
                    else -> {}
                }
            }
        }
    }
    
    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun showToastMessage(message: String){
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }

    fun showTwoButtonDialog(
        title: String,
        description: String,
        confirmBtnClickListener: () -> Unit,
    ){
        twoButtonDialog = TwoButtonDialog(requireContext(), title, description, confirmBtnClickListener)
        twoButtonDialog.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}