package com.avengers.nibobnebob.presentation.ui.main.global.userdetail

import androidx.fragment.app.activityViewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentUserDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class UserDetailFragment: BaseFragment<FragmentUserDetailBinding>(R.layout.fragment_user_detail) {

    override val parentViewModel: MainViewModel by activityViewModels()

}