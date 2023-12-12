package com.avengers.nibobnebob.presentation.base

import android.content.Context
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
import com.avengers.nibobnebob.presentation.customview.LoadingDialog
import com.avengers.nibobnebob.presentation.customview.OneButtonTitleDialog
import com.avengers.nibobnebob.presentation.customview.TwoButtonTitleDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val parentViewModel: BaseActivityViewModel

    private lateinit var twoButtonTitleDialog: TwoButtonTitleDialog
    private lateinit var oneButtonTitleDialog: OneButtonTitleDialog
    private lateinit var loadingDialog: LoadingDialog
    private var currentSnackbar: Snackbar? = null
    private var loadingState = false

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

        initView()
        initEventObserver()

        repeatOnStarted {
            parentViewModel.networkState.collectLatest {
                when (it) {
                    NetWorkState.NETWORK_DISCONNECTED -> {
                        showSnackBar(resources.getString(R.string.no_network_text), "재시도")
                    }

                    NetWorkState.NETWORK_CONNECTED -> {
                        currentSnackbar?.dismiss()
                        initNetworkView()
                    }

                    else -> {}
                }
            }
        }
    }

    abstract fun initView()

    abstract fun initEventObserver()

    abstract fun initNetworkView()

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showTwoButtonTitleDialog(
        title: String,
        description: String,
        confirmBtnClickListener: () -> Unit,
    ) {
        twoButtonTitleDialog =
            TwoButtonTitleDialog(requireContext(), title, description, confirmBtnClickListener)
        twoButtonTitleDialog.show()
    }

    fun showOneButtonTitleDialog(
        title: String,
        confirmBtnClickListener: () -> Unit,
    ) {
        oneButtonTitleDialog =
            OneButtonTitleDialog(requireContext(), title, confirmBtnClickListener)
        oneButtonTitleDialog.show()
    }

    fun showSnackBar(text: String, action: String? = null) {

        currentSnackbar = Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG
        ).apply {
            action?.let {
                setAction(it) {
                    dismiss()
                }
            }
            show()
        }
    }

    fun showLoading(context: Context) {
        if (!loadingState) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
            loadingState = true
        }
    }

    fun dismissLoading() {
        if (loadingState) {
            loadingDialog.dismiss()
            loadingState = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingState) {
            loadingDialog.dismiss()
        }
        _binding = null
    }

}