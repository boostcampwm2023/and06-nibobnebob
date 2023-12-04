package com.avengers.nibobnebob.presentation.ui.intro.signup

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentBasicSignupBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicSignupFragment: BaseFragment<FragmentBasicSignupBinding>(R.layout.fragment_basic_signup) {

    override val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: BasicSignupViewModel by viewModels()

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initNetworkView() {
        binding.vm = viewModel
    }

    override fun initEventObserver() {
        TODO("Not yet implemented")
    }


}