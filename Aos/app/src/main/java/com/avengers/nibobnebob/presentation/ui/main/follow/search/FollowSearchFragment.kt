package com.avengers.nibobnebob.presentation.ui.main.follow.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowSearchBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowSearchFragment: BaseFragment<FragmentFollowSearchBinding>(R.layout.fragment_follow_search) {
    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowSearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}