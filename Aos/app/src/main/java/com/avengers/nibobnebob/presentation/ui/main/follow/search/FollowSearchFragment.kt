package com.avengers.nibobnebob.presentation.ui.main.follow.search

import androidx.fragment.app.activityViewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel

class FollowSearchFragment: BaseFragment<FragmentFollowSearchBinding>(R.layout.fragment_follow_search) {
    override val parentViewModel: MainViewModel by activityViewModels()
}