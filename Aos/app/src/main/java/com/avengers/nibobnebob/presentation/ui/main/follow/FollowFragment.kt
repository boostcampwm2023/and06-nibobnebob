package com.avengers.nibobnebob.presentation.ui.main.follow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.FragmentFollowBinding
import com.avengers.nibobnebob.presentation.base.BaseFragment
import com.avengers.nibobnebob.presentation.ui.main.MainViewModel
import com.avengers.nibobnebob.presentation.ui.main.follow.adapter.FollowAdapter

class FollowFragment : BaseFragment<FragmentFollowBinding>(R.layout.fragment_follow) {

    override val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: FollowViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvFollowList.adapter = FollowAdapter()
    }

}