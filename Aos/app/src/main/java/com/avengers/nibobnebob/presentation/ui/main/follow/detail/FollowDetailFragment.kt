package com.avengers.nibobnebob.presentation.ui.main.follow.detail

import androidx.fragment.app.activityViewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowDetailBinding
import com.avengers.nibobnebob.presentation.base.BaseActivityViewModel
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class FollowDetailFragment: BaseFragment<FragmentFollowDetailBinding>(R.layout.fragment_follow_detail) {

    override val parentViewModel: MainViewModel by activityViewModels()

}