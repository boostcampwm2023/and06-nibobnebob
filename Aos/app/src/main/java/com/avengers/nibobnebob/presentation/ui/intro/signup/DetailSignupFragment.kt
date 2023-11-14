package com.avengers.nibobnebob.presentation.ui.intro.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentDetailSignupBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment

class DetailSignupFragment: BaseFragment<FragmentDetailSignupBinding>(R.layout.fragment_detail_signup) {

    private val viewModel: DetailSignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
    }
}